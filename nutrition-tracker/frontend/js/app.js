let macroChart = null;
let trendChart = null;
let isLoginMode = true;

// Initialize App
document.addEventListener('DOMContentLoaded', async () => {
    setGreeting();
    setupAuthForm();
    setupForm();
    setupTabs();
    
    document.getElementById('logoutBtn').addEventListener('click', () => {
        API.logout();
        showAuthModal();
        renderDashboard([]); // clear dashboard
    });

    if (!localStorage.getItem('authToken')) {
        showAuthModal();
    } else {
        await tryLoadData();
    }
});

function showAuthModal() {
    document.getElementById('authOverlay').classList.add('active');
}

function hideAuthModal() {
    document.getElementById('authOverlay').classList.remove('active');
    document.getElementById('authForm').reset();
}

function setupTabs() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            // Remove active class from all buttons and contents
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));

            // Add active class to clicked button
            btn.classList.add('active');

            // Show corresponding tab content
            const targetId = btn.getAttribute('data-tab');
            const targetContent = document.getElementById(targetId);
            if (targetContent) {
                targetContent.classList.add('active');
            }
        });
    });
}

function setupAuthForm() {
    const authForm = document.getElementById('authForm');
    const toggleBtn = document.getElementById('authToggleBtn');
    
    toggleBtn.addEventListener('click', () => {
        isLoginMode = !isLoginMode;
        document.getElementById('authTitle').textContent = isLoginMode ? 'Welcome Back' : 'Create Account';
        document.getElementById('authSubtitle').textContent = isLoginMode ? 'Please log in to your account.' : 'Sign up to start tracking.';
        document.getElementById('authSubmitBtn').textContent = isLoginMode ? 'Log In' : 'Sign Up';
        document.getElementById('authToggleText').textContent = isLoginMode ? "Don't have an account?" : "Already have an account?";
        toggleBtn.textContent = isLoginMode ? 'Sign Up' : 'Log In';
    });

    authForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const user = document.getElementById('username').value;
        const pass = document.getElementById('password').value;
        const btn = document.getElementById('authSubmitBtn');
        const originalText = btn.textContent;
        btn.textContent = 'Please wait...';
        btn.disabled = true;

        try {
            if (isLoginMode) {
                await API.login(user, pass);
                hideAuthModal();
                setGreeting();
                await tryLoadData();
            } else {
                await API.signup(user, pass);
                // auto login after signup
                await API.login(user, pass);
                hideAuthModal();
                setGreeting();
                await tryLoadData();
            }
        } catch (error) {
            alert('Authentication Error: ' + error.message + '\n\nMake sure the Java backend is running!');
        } finally {
            btn.textContent = originalText;
            btn.disabled = false;
        }
    });
}

// Friendly greeting based on time of day
function setGreeting() {
    const hour = new Date().getHours();
    const greetingEl = document.getElementById('greeting');
    
    let username = '';
    const token = localStorage.getItem('authToken');
    if (token) {
        try {
            const decoded = atob(token);
            username = decoded.split(':')[0];
            username = ', ' + username.charAt(0).toUpperCase() + username.slice(1);
        } catch(e) {}
    }

    if (hour < 12) greetingEl.textContent = `Good morning${username}! 🌅`;
    else if (hour < 18) greetingEl.textContent = `Good afternoon${username}! ☀️`;
    else greetingEl.textContent = `Good evening${username}! 🌙`;

    // Set today's date
    const options = { weekday: 'long', month: 'short', day: 'numeric' };
    document.getElementById('currentDate').textContent = new Date().toLocaleDateString('en-US', options);
}

// Setup Form Submission
function setupForm() {
    const form = document.getElementById('foodForm');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const btn = form.querySelector('.submit-btn');
        const originalText = btn.textContent;
        btn.textContent = 'Saving...';
        btn.disabled = true;

        const foodEntry = {
            name: document.getElementById('foodName').value,
            calories: parseInt(document.getElementById('calories').value),
            protein: parseFloat(document.getElementById('protein').value),
            carbs: parseFloat(document.getElementById('carbs').value),
            fat: parseFloat(document.getElementById('fat').value)
        };

        try {
            await API.addFood(foodEntry);
            form.reset();
            await tryLoadData();
        } catch (error) {
            if (error.message === 'Unauthorized') {
                API.logout();
                showAuthModal();
            } else {
                console.warn('Backend connection failed, using LocalStorage fallback.');
                simulateLocalAdd(foodEntry);
                form.reset();
            }
        } finally {
            btn.textContent = originalText;
            btn.disabled = false;
        }
    });

    const feedbackForm = document.getElementById('feedbackForm');
    if (feedbackForm) {
        feedbackForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const btn = document.getElementById('feedbackSubmitBtn');
            const originalText = btn.textContent;
            btn.textContent = 'Sending...';
            btn.disabled = true;

            const name = document.getElementById('feedbackName').value;
            const message = document.getElementById('feedbackMessage').value;
            const ratingInput = document.querySelector('input[name="rating"]:checked');
            const rating = ratingInput ? parseInt(ratingInput.value) : 0;

            try {
                await API.submitFeedback({ name, message, rating });
                alert('Thank you for your feedback! We appreciate your input.');
                feedbackForm.reset();
            } catch (error) {
                alert('Failed to submit feedback. ' + error.message);
            } finally {
                btn.textContent = originalText;
                btn.disabled = false;
            }
        });
    }
}

async function tryLoadData() {
    try {
        await loadAndRenderData();
        await renderRecommendations();
        await renderAnalytics();
    } catch (e) {
        if (e.message === 'Unauthorized') {
            API.logout();
            showAuthModal();
        }
    }
}

// LocalStorage simulation fallback
function getLocalData() {
    const data = localStorage.getItem('nutritionData');
    return data ? JSON.parse(data) : [];
}

function saveLocalData(data) {
    localStorage.setItem('nutritionData', JSON.stringify(data));
}

function simulateLocalAdd(entry) {
    entry.id = Date.now();
    const currentData = getLocalData();
    currentData.push(entry);
    saveLocalData(currentData);
    renderDashboard(currentData);
}

async function loadAndRenderData() {
    const localData = getLocalData();
    const foods = await API.getTodayFoods();
    
    if (foods.length === 0 && localData.length > 0) {
        // Show LocalStorage data if backend is empty or down
        renderDashboard(localData);
    } else if (foods.length > 0) {
        // Backend overrides local if it has data
        renderDashboard(foods);
    } else {
        renderDashboard([]);
    }
}

function renderDashboard(foods) {
    let totalCals = 0, totalPro = 0, totalCarbs = 0, totalFat = 0;

    const listContainer = document.getElementById('foodList');
    listContainer.innerHTML = '';

    if (foods.length === 0) {
        listContainer.innerHTML = `
            <tr><td colspan="6" class="empty-diary">No foods logged yet.</td></tr>
        `;
    }

    foods.forEach(food => {
        totalCals += food.calories;
        totalPro += food.protein;
        totalCarbs += food.carbs;
        totalFat += food.fat;

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="food-name">${food.name}</td>
            <td class="text-right">${food.calories}</td>
            <td class="text-right">${Math.round(food.protein * 10) / 10}</td>
            <td class="text-right">${Math.round(food.carbs * 10) / 10}</td>
            <td class="text-right">${Math.round(food.fat * 10) / 10}</td>
            <td class="text-right">
                <button class="delete-btn" onclick="deleteEntry(${food.id})" title="Remove meal">✖</button>
            </td>
        `;
        listContainer.appendChild(tr);
    });

    // Update Summary Cards
    document.getElementById('totalCalories').textContent = Math.round(totalCals);
    document.getElementById('totalProtein').textContent = Math.round(totalPro * 10) / 10 + 'g';
    document.getElementById('totalCarbs').textContent = Math.round(totalCarbs * 10) / 10 + 'g';
    document.getElementById('totalFat').textContent = Math.round(totalFat * 10) / 10 + 'g';

    updateProgressBars(totalCals, totalPro, totalCarbs, totalFat);
}

function updateProgressBars(cals, pro, carbs, fat) {
    const TARGETS = { cals: 2000, pro: 150, carbs: 200, fat: 65 };

    const calPct = Math.min((cals / TARGETS.cals) * 100, 100);
    const proPct = Math.min((pro / TARGETS.pro) * 100, 100);
    const carbPct = Math.min((carbs / TARGETS.carbs) * 100, 100);
    const fatPct = Math.min((fat / TARGETS.fat) * 100, 100);

    document.getElementById('calProgress').style.width = `${calPct}%`;
    document.getElementById('proProgress').style.width = `${proPct}%`;
    document.getElementById('carbProgress').style.width = `${carbPct}%`;
    document.getElementById('fatProgress').style.width = `${fatPct}%`;

    if (cals > TARGETS.cals) document.getElementById('calProgress').style.backgroundColor = 'var(--color-danger)';
    else document.getElementById('calProgress').style.backgroundColor = 'var(--color-calories)';
    
    if (pro >= TARGETS.pro) document.getElementById('proProgress').style.backgroundColor = 'var(--color-success)';
    else document.getElementById('proProgress').style.backgroundColor = 'var(--color-protein)';
}

// Global Delete Function
window.deleteEntry = async (id) => {
    try {
        await API.deleteFood(id);
    } catch(e) {
        if (e.message === 'Unauthorized') {
            API.logout();
            showAuthModal();
        } else {
            // Handle LocalStorage fallback deletion
            let localData = getLocalData();
            localData = localData.filter(f => f.id !== id);
            saveLocalData(localData);
        }
    }
    await tryLoadData();
};

async function renderRecommendations() {
    const listContainer = document.getElementById('suggestionList');
    if (!listContainer) return;
    
    listContainer.innerHTML = '';
    const recommendations = await API.getRecommendations();
    
    if (recommendations.length === 0) {
        listContainer.innerHTML = `<p style="font-size: 0.9rem; color: var(--text-muted); text-align: center; padding: 1rem;">Log more meals to unlock smart suggestions!</p>`;
        return;
    }

    recommendations.forEach((rec) => {
        const item = document.createElement('div');
        item.className = 'suggestion-item';
        item.innerHTML = `
            <div class="suggestion-reason">💡 ${rec.reason}</div>
            <div class="suggestion-header">
                <div>
                    <h4>${rec.foodName}</h4>
                    <div class="food-macros" style="margin-top: 4px;">
                        <span style="color: var(--text-main)">🔥 ${rec.calories}</span>
                        <span style="color: var(--protein-color)">P: ${rec.protein}g</span>
                        <span style="color: var(--carbs-color)">C: ${rec.carbs}g</span>
                        <span style="color: var(--fat-color)">F: ${rec.fat}g</span>
                    </div>
                </div>
                <button class="add-suggestion-btn" onclick='addSuggestion(${JSON.stringify(rec).replace(/'/g, "\\'")})'>+ Add</button>
            </div>
        `;
        listContainer.appendChild(item);
    });
}

window.addSuggestion = async (rec) => {
    try {
        await API.addFood({
            name: rec.foodName,
            calories: rec.calories,
            protein: rec.protein,
            carbs: rec.carbs,
            fat: rec.fat
        });
        await tryLoadData();
    } catch (e) {
        alert("Failed to add suggestion.");
    }
};

async function renderAnalytics() {
    const analyticsContainer = document.querySelector('.analytics-section');
    if (!analyticsContainer) return;

    try {
        const data = await API.getWeeklyAnalytics();
        if (!data) return;

        document.getElementById('avgWeeklyCals').textContent = data.averageDailyCalories;
        document.getElementById('highestCalFoodName').textContent = data.highestCalorieFoodName;
        document.getElementById('highestCalFoodAmount').textContent = data.highestCalorieFoodAmount + ' kcal';

        const ctx = document.getElementById('trendChart').getContext('2d');
        
        const labels = data.dailyTrends.map(d => d.date);
        const proteinData = data.dailyTrends.map(d => d.protein);
        const carbsData = data.dailyTrends.map(d => d.carbs);
        const fatData = data.dailyTrends.map(d => d.fat);

        if (trendChart) {
            trendChart.destroy();
        }

        trendChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Protein (g)',
                        data: proteinData,
                        backgroundColor: '#34d399',
                        borderRadius: 4
                    },
                    {
                        label: 'Carbs (g)',
                        data: carbsData,
                        backgroundColor: '#fbbf24',
                        borderRadius: 4
                    },
                    {
                        label: 'Fat (g)',
                        data: fatData,
                        backgroundColor: '#f87171',
                        borderRadius: 4
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        stacked: true,
                        grid: { color: 'rgba(255, 255, 255, 0.05)' },
                        ticks: { color: '#94a3b8', font: { family: 'Outfit' } }
                    },
                    y: {
                        stacked: true,
                        grid: { color: 'rgba(255, 255, 255, 0.05)' },
                        ticks: { color: '#94a3b8', font: { family: 'Outfit' } }
                    }
                },
                plugins: {
                    legend: {
                        position: 'top',
                        labels: { color: '#f8fafc', font: { family: 'Outfit' } }
                    }
                }
            }
        });
    } catch (e) {
        console.warn("Analytics could not be loaded.");
    }
}

// UPDATE THIS URL after deploying your backend to Render!
const PROD_API_URL = 'https://nutrition-tracker-backend-w67m.onrender.com/api';

const API_BASE_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1' || window.location.protocol === 'file:'
    ? 'http://localhost:8085/api' 
    : PROD_API_URL;

const API = {
    getAuthHeader() {
        const token = localStorage.getItem('authToken');
        return token ? { 'Authorization': `Basic ${token}` } : {};
    },

    async signup(username, password) {
        const response = await fetch(`${API_BASE_URL}/auth/signup`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        const data = await response.json();
        if (!response.ok) throw new Error(data.error || 'Signup failed');
        return data;
    },

    async login(username, password) {
        const token = btoa(`${username}:${password}`);
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Authorization': `Basic ${token}` }
        });
        if (!response.ok) throw new Error('Invalid username or password');
        
        // Store token
        localStorage.setItem('authToken', token);
        return true;
    },

    logout() {
        localStorage.removeItem('authToken');
        // Clear local fallback data just in case
        localStorage.removeItem('nutritionData');
    },

    async getTodayFoods() {
        try {
            const response = await fetch(`${API_BASE_URL}/foods`, {
                headers: this.getAuthHeader()
            });
            if (response.status === 401) throw new Error('Unauthorized');
            if (!response.ok) throw new Error('Failed to fetch data');
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            if (error.message === 'Unauthorized') throw error;
            return []; // fallback for down server
        }
    },

    async getRecommendations() {
        try {
            const response = await fetch(`${API_BASE_URL}/recommendations`, {
                headers: this.getAuthHeader()
            });
            if (!response.ok) return [];
            return await response.json();
        } catch (error) {
            return [];
        }
    },

    async getWeeklyAnalytics() {
        try {
            const response = await fetch(`${API_BASE_URL}/analytics/weekly`, {
                headers: this.getAuthHeader()
            });
            if (!response.ok) return null;
            return await response.json();
        } catch (error) {
            return null;
        }
    },

    async addFood(foodEntry) {
        try {
            const response = await fetch(`${API_BASE_URL}/foods`, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    ...this.getAuthHeader()
                },
                body: JSON.stringify(foodEntry)
            });
            if (response.status === 401) throw new Error('Unauthorized');
            if (!response.ok) throw new Error('Failed to add food');
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },

    async deleteFood(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/foods/${id}`, {
                method: 'DELETE',
                headers: this.getAuthHeader()
            });
            if (response.status === 401) throw new Error('Unauthorized');
            if (!response.ok) throw new Error('Failed to delete food');
            return true;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },

    async submitFeedback(feedback) {
        try {
            const response = await fetch(`${API_BASE_URL}/feedback`, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    ...this.getAuthHeader() 
                },
                body: JSON.stringify(feedback)
            });
            if (!response.ok) throw new Error('Failed to submit feedback');
            return await response.json();
        } catch (error) {
            console.error('Feedback Error:', error);
            throw error;
        }
    }
};

import type { Game, Player, ConnectRequest, MoveRequest } from '../types/game';

const API_BASE_URL = 'http://localhost:8080/api/game';

export const gameService = {
    async createGame(player: Player): Promise<Game> {
        const response = await fetch(`${API_BASE_URL}/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(player),
        });
        if (!response.ok) throw new Error('Failed to create game');
        return response.json();
    },

    async connectToGame(request: ConnectRequest): Promise<Game> {
        const response = await fetch(`${API_BASE_URL}/connect`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request),
        });
        if (!response.ok) throw new Error('Failed to connect to game');
        return response.json();
    },

    async makeMove(request: MoveRequest): Promise<Game> {
        const response = await fetch(`${API_BASE_URL}/move`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request),
        });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to make move');
        }
        return response.json();
    },

    async getGameStatus(gameId: string): Promise<Game> {
        const response = await fetch(`${API_BASE_URL}/${gameId}`);
        if (!response.ok) throw new Error('Failed to fetch game status');
        return response.json();
    }
};

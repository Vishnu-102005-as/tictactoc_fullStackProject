export type Mark = 'X' | 'O' | 'EMPTY';

export type GameStatus = 'WAITING_FOR_PLAYER' | 'IN_PROGRESS' | 'FINISHED_X_WINS' | 'FINISHED_O_WINS' | 'DRAW';

export interface Player {
    username: string;
}

export interface Game {
    gameId: string;
    player1: Player;
    player2: Player | null;
    status: GameStatus;
    board: Mark[][];
    currentTurn: Mark;
}

export interface ConnectRequest {
    player: Player;
    gameId: string;
}

export interface MoveRequest {
    gameId: string;
    playerMark: Mark;
    row: number;
    col: number;
}

import { useState, useEffect, useCallback } from 'react';
import type { Game, Mark, Player } from './types/game';
import { gameService } from './api/gameService';
import './App.css';

function App() {
  const [username, setUsername] = useState('');
  const [gameId, setGameId] = useState('');
  const [game, setGame] = useState<Game | null>(null);
  const [myMark, setMyMark] = useState<Mark | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isConnecting, setIsConnecting] = useState(false);

  const refreshGame = useCallback(async () => {
    if (!game) return;
    try {
      const updatedGame = await gameService.getGameStatus(game.gameId);
      setGame(updatedGame);
      
      // Determine my mark if not set (reconnecting case)
      if (!myMark) {
        if (updatedGame.player1.username === username) setMyMark('X');
        else if (updatedGame.player2?.username === username) setMyMark('O');
      }
    } catch (err) {
      console.error('Polling error:', err);
    }
  }, [game, myMark, username]);

  // Polling logic
  useEffect(() => {
    let interval: number | undefined;
    if (game && (game.status === 'WAITING_FOR_PLAYER' || (game.status === 'IN_PROGRESS' && game.currentTurn !== myMark))) {
      interval = setInterval(refreshGame, 2000);
    }
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [game, myMark, refreshGame]);

  const handleCreateGame = async () => {
    if (!username) {
      setError('Please enter a username');
      return;
    }
    setError(null);
    setIsConnecting(true);
    try {
      const newGame = await gameService.createGame({ username });
      setGame(newGame);
      setMyMark('X');
    } catch (err: any) {
      setError(err.message);
    } finally {
      setIsConnecting(false);
    }
  };

  const handleConnectGame = async () => {
    if (!username || !gameId) {
      setError('Please enter username and Game ID');
      return;
    }
    setError(null);
    setIsConnecting(true);
    try {
      const joinedGame = await gameService.connectToGame({
        player: { username },
        gameId: gameId
      });
      setGame(joinedGame);
      setMyMark('O');
    } catch (err: any) {
      setError(err.message);
    } finally {
      setIsConnecting(false);
    }
  };

  const handleMove = async (row: number, col: number) => {
    if (!game || !myMark || game.status !== 'IN_PROGRESS' || game.currentTurn !== myMark) return;
    
    try {
      const updatedGame = await gameService.makeMove({
        gameId: game.gameId,
        playerMark: myMark,
        row,
        col
      });
      setGame(updatedGame);
      setError(null);
    } catch (err: any) {
      setError(err.message);
    }
  };

  const copyGameId = () => {
    if (game) {
      navigator.clipboard.writeText(game.gameId);
      alert('Game ID copied to clipboard!');
    }
  };

  if (!game) {
    return (
      <div className="setup-container glass-card">
        <h1>TicTacToe</h1>
        <div className="input-group">
          <label>Username</label>
          <input 
            type="text" 
            placeholder="Enter your name" 
            value={username} 
            onChange={(e) => setUsername(e.target.value)} 
          />
        </div>
        
        <div className="input-group">
          <button className="btn btn-primary" onClick={handleCreateGame} disabled={isConnecting}>
            {isConnecting ? 'Creating...' : 'Create New Game'}
          </button>
        </div>

        <div style={{ margin: '2rem 0', textAlign: 'center', color: 'var(--text-dim)' }}>— OR —</div>

        <div className="input-group">
          <label>Game ID</label>
          <input 
            type="text" 
            placeholder="Paste Game ID to join" 
            value={gameId} 
            onChange={(e) => setGameId(e.target.value)} 
          />
          <button className="btn btn-outline" onClick={handleConnectGame} disabled={isConnecting}>
            {isConnecting ? 'Joining...' : 'Join Game'}
          </button>
        </div>

        {error && <p style={{ color: 'var(--accent-x)', marginTop: '1rem', textAlign: 'center' }}>{error}</p>}
      </div>
    );
  }

  const getStatusText = () => {
    switch (game.status) {
      case 'WAITING_FOR_PLAYER': return 'Waiting for opponent...';
      case 'IN_PROGRESS': return game.currentTurn === myMark ? 'Your turn!' : "Opponent's turn...";
      case 'FINISHED_X_WINS': return game.player1.username + ' (X) Wins!';
      case 'FINISHED_O_WINS': return (game.player2?.username || 'O') + ' Wins!';
      case 'DRAW': return "It's a Draw!";
      default: return '';
    }
  };

  return (
    <div className="glass-card" style={{ maxWidth: '600px', margin: '0 auto' }}>
      <h1>TicTacToe</h1>
      
      <div className="game-info">
        <div className="player-badge">
          <span className="name">{game.player1.username}</span>
          <span className="mark">Player X</span>
        </div>
        <div className="status-badge">
          {getStatusText()}
        </div>
        <div className="player-badge" style={{ alignItems: 'flex-end' }}>
          <span className="name">{game.player2?.username || 'Waiting...'}</span>
          <span className="mark">Player O</span>
        </div>
      </div>

      <div className="board">
        {game.board.map((row, rowIndex) => 
          row.map((cell, colIndex) => (
            <div 
              key={`${rowIndex}-${colIndex}`} 
              className={`cell ${cell} ${cell !== 'EMPTY' ? 'occupied' : ''}`}
              onClick={() => handleMove(rowIndex, colIndex)}
            >
              {cell === 'EMPTY' ? '' : cell}
            </div>
          ))
        )}
      </div>

      <div className="game-id-badge" onClick={copyGameId} title="Click to copy">
        ID: {game.gameId}
      </div>

      {error && <p style={{ color: 'var(--accent-x)', marginTop: '1rem', textAlign: 'center' }}>{error}</p>}
      
      <button className="btn btn-outline" onClick={() => { setGame(null); setMyMark(null); }}>
        Leave Game
      </button>
    </div>
  );
}

export default App;

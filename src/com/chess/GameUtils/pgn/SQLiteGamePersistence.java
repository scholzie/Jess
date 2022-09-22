package com.chess.GameUtils.pgn;

import com.chess.GameUtils.Game;
import com.chess.engine.board.Board;
import com.chess.engine.board.move.Move;
import com.chess.engine.player.Player;

import java.sql.*;

public class SQLiteGamePersistence implements PGNPersistence {

    private final Connection dbConnection;

    private static final SQLiteGamePersistence INSTANCE = new SQLiteGamePersistence();
    private static final String JDBC_DRIVER = "com.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:db/book.db";
    private static final String NEXT_BEST_MOVE_QUERY =
            "SELECT substr(g1.moves, LENGTH('%s') + %d, INSTR(SUBSTR(g1.moves, LENGTH('%s') + %d, LENGTH(g1.moves)), ',') - 1), " +
                    "COUNT(*) FROM game g1 WHERE g1.moves LIKE '%s%%' AND (g1.outcome = '%s') GROUP BY substr(g1.moves, LENGTH('%s') + %d, " +
                    "INSTR(substr(g1.moves, LENGTH('%s') + %d, LENGTH(g1.moves)), ',') - 1) ORDER BY 2 DESC";

    private SQLiteGamePersistence() {
        this.dbConnection = createDbConnection();
        createGameTable();
        createIndex("outcome", "OutcomeIndex");
        createIndex("moves", "MoveIndex");
    }

    // TODO implement for SQLite
    private void createIndex(final String colName, final String indexName) {
        try {
//            final String sql = "SELECT * FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_CATALOG = 'def' AND " +
//                    "TABLE_SCHEMA = DATABASE() AND TABLE_NAME = \"game\" and INDEX_NAME = \"" + indexName + "\"";
//            final Statement gameStatement = this.dbConnection.createStatement();
//            gameStatement.execute(sql);
//
//            final ResultSet resultSet = gameStatement.getResultSet();
//            if(!resultSet.isBeforeFirst()) {
                final Statement indexStatement = this.dbConnection.createStatement();
                indexStatement.execute("CREATE INDEX IF NOT EXISTS " + indexName + " on Game(" + colName + ");\n");
                indexStatement.close();
//            }
//            gameStatement.close();

        } catch (SQLException e) {
            System.out.println("CREATE INDEX IF NOT EXISTS " +indexName+ " on Game(" + colName + ");\n");
            e.printStackTrace();
        }
    }

    private void createGameTable() {
        try {
            final Statement statement = this.dbConnection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS Game(id int primary key, outcome varchar(10), moves varchar(3072));");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SQLiteGamePersistence get() {
        return INSTANCE;
    }

    private static Connection createDbConnection() {
        try {
//            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void persistGame(final Game game) {
        executePersist(game);
    }

    private void executePersist(final Game game) {
        final String gameSqlString = "INSERT INTO Game(id, outcome, moves) VALUES(?, ?, ?)";
        try {
            final PreparedStatement gameStatement = this.dbConnection.prepareStatement(gameSqlString);
            gameStatement.setInt(1, getMaxGameRow() + 1);
            gameStatement.setString(2, game.getWinner().toUpperCase());;
            gameStatement.setString(3, game.getMoves().toString()
                                                                    .replaceAll("\\[", "")
                                                                    .replaceAll("\\]", ""));
            gameStatement.executeUpdate();
            gameStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getMaxGameRow() {
        int maxId = 0;
        final String sqlString = "SELECT MAX(ID) from Game";
        try {
            final Statement gameStatement = this.dbConnection.createStatement();
            gameStatement.execute(sqlString);
            final ResultSet result = gameStatement.getResultSet();
            if (result.next()) {
                maxId = result.getInt(1);
            }
            gameStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return maxId;
    }

    @Override
    public Move getNextBestMove(final Board board, final Player player, final String gameText) {
        return queryBestMove(board, player, gameText);
    }

    private Move queryBestMove(final Board board, final Player player, final String gameText) {
        String bestMove = "";
        String count = "0";
        try {
            final int offSet = gameText.isEmpty() ? 1 : 3;
            final String sqlString = String.format(NEXT_BEST_MOVE_QUERY, gameText, offSet, gameText, offSet, gameText,
                    player.getAlliance().name().toUpperCase(), gameText, offSet, gameText, offSet);
            System.out.println(sqlString);
            final Statement gameStatement = this.dbConnection.createStatement();
            gameStatement.execute(sqlString);
            final ResultSet rs2 = gameStatement.getResultSet();
            if(rs2.next()) {
                bestMove = rs2.getString(1);
                count = rs2.getString(2);
            }
            gameStatement.close();
        }
        catch (final SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\tselected book move = " +bestMove+ " with " +count+ " hits");
        return PGNUtilities.createMove(board, bestMove);
    }
}

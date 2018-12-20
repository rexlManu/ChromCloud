package me.rexlmanu.chromcloudnode.database;

import me.rexlmanu.chromcloudnode.ChromCloudNode;
import me.rexlmanu.chromcloudnode.configuration.DefaultConfig;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;

public final class DatabaseManager {

    private ExecutorService executor;
    private MySQL sql;

    private DatabaseManager(final String host, final int port, final String user, final String password, final String database) throws Exception {
        this.sql = new MySQL(host, port, user, password, database);
        this.executor = Executors.newCachedThreadPool();
    }

    public DatabaseManager() {
        this.executor = Executors.newCachedThreadPool();
    }

    public void init(DefaultConfig defaultConfig) {
        try {
            this.sql = new MySQL(defaultConfig.getMySqlHostname(), defaultConfig.getMySqlPort(), defaultConfig.getMySqlUsername(), defaultConfig.getMySqlPassword(), defaultConfig.getMySqlDatabase());
        } catch (Exception e) {
            ChromCloudNode.getInstance().getChromLogger().doLog(Level.SEVERE, e.getMessage());
        }
    }

    public void update(final PreparedStatement statement) {
        this.executor.execute(() -> this.sql.queryUpdate(statement));
    }

    public void update(final String statement) {
        this.executor.execute(() -> this.sql.queryUpdate(statement));
    }

    public void query(final PreparedStatement statement, final Consumer<ResultSet> consumer) {
        this.executor.execute(() -> consumer.accept(this.sql.query(statement)));
    }

    public void query(final String statement, final Consumer<ResultSet> consumer) {
        this.executor.execute(() -> consumer.accept(this.sql.query(statement)));
    }

    public PreparedStatement prepare(final String query) {
        try {
            return this.sql.getConnection().prepareStatement(query);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MySQL getMySQL() {
        return this.sql;
    }

    public static class MySQL {

        private final String host;
        private final String user;
        private final String password;
        private final String database;
        private final int port;

        private Connection conn;

        public MySQL(final String host, final int port, final String user, final String password, final String database) throws Exception {
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
            this.database = database;

            this.openConnection();
        }

        public void queryUpdate(final String query) {
            try (final PreparedStatement statement = this.conn.prepareStatement(query)) {
                this.queryUpdate(statement);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        public void queryUpdate(final PreparedStatement statement) {
            try {
                statement.executeUpdate();
            } catch (final Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    statement.close();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public ResultSet query(final String query) {
            this.checkConnection();
            try {
                return this.query(this.conn.prepareStatement(query));
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public ResultSet query(final PreparedStatement statement) {
            this.checkConnection();
            try {
                return statement.executeQuery();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public Connection getConnection() {
            return this.conn;
        }

        private void checkConnection() {
        }

        public boolean isConnected(){
            try {
                return this.conn!=null && !this.conn.isClosed();
            } catch (SQLException e) {
                return false;
            }
        }

        public Connection openConnection() throws Exception {
            Class.forName("com.mysql.jdbc.Driver");
            return this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
        }

        public void closeConnection() {
            try {
                this.conn.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            } finally {
                this.conn = null;
            }
        }
    }

}

package se.alten.demo;

import org.testcontainers.containers.PostgreSQLContainer;

class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

    private static final String IMAGE = "postgres:latest";
    private static PostgresContainer postgreSQLContainer;

    private PostgresContainer() {
        super(IMAGE);
    }

    public static PostgresContainer getInstance() {
        if ( postgreSQLContainer == null ) {
            postgreSQLContainer = new PostgresContainer();
        }
        return postgreSQLContainer;
    }

    @Override
    public void start() {
        super.start();
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String a = jdbcUrl.substring(0,5);
        String b = jdbcUrl.substring(5,33);
        String testJdbcUrl = (a + "tc:" + b);
        System.setProperty("DB_URL", testJdbcUrl);
        System.setProperty("DB_USERNAME", postgreSQLContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgreSQLContainer.getPassword());
    }

    @Override
    public void stop() {}
}
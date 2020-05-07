package se.alten.demo;

import org.testcontainers.containers.PostgreSQLContainer;

class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

    private static final String IMAGE = "mysql";
    private static PostgresContainer postgresContainer;

    private PostgresContainer() {
        super(IMAGE);
    }

    public static PostgresContainer getInstance() {
        if ( postgresContainer == null ) {
            postgresContainer = new PostgresContainer();
        }
        return postgresContainer;
    }

    @Override
    public void start() {
        super.start();
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String a = jdbcUrl.substring(0,5);
        String b = jdbcUrl.substring(5,33);
        String testJdbcUrl = (a + "tc:" + b);
        System.setProperty("DB_URL", testJdbcUrl);
        System.setProperty("DB_USERNAME", postgresContainer.getUsername());
        System.setProperty("DB_PASSWORD", postgresContainer.getPassword());
    }

    @Override
    public void stop() {}
}

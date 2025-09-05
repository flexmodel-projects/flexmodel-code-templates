package tech.wetech.flexmodel.codegen;


import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.wetech.flexmodel.session.SessionFactory;
import tech.wetech.flexmodel.sql.JdbcDataSourceProvider;

/**
 * @author cjbi
 */
public class AbstractIntegrationTest {

  protected static final Logger log = LoggerFactory.getLogger(AbstractIntegrationTest.class);
  protected static final String SCHEMA_NAME = "system";
  protected static SessionFactory sessionFactory;
  protected static CodeGenerationService codeGenerationService;

  @BeforeAll
  static void init() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl("jdbc:sqlite:file::memory:?cache=shared");
    JdbcDataSourceProvider jdbcDataSourceProvider = new JdbcDataSourceProvider("system", dataSource);
    sessionFactory = SessionFactory.builder()
      .setDefaultDataSourceProvider(jdbcDataSourceProvider)
      .build();
    sessionFactory.loadScript("system", "import.idl");
    codeGenerationService = new CodeGenerationService(sessionFactory);
  }

  @AfterAll
  static void destroy() {
  }

}


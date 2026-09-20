package com.generated.waterLeak;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/** 测试用内嵌 PostgreSQL：真实执行 database/init.sql（含部分唯一索引与种子数据）。 */
@TestConfiguration
public class EmbeddedPostgresConfig {

  @Bean(destroyMethod = "close")
  public EmbeddedPostgres embeddedPostgres() throws Exception {
    EmbeddedPostgres pg = EmbeddedPostgres.start();
    try (var conn = pg.getPostgresDatabase().getConnection(); var st = conn.createStatement()) {
      st.execute(java.nio.file.Files.readString(
          java.nio.file.Path.of("../database/init.sql")));
    }
    return pg;
  }

  @Bean
  @Primary
  public DataSource dataSource(EmbeddedPostgres embeddedPostgres) {
    return embeddedPostgres.getPostgresDatabase();
  }
}

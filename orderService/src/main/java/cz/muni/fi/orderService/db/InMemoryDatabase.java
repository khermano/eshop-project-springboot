package cz.muni.fi.orderService.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;

@Configuration
public class InMemoryDatabase {
	@Bean
	public DataSource db(){
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.DERBY).setName("orderService").build();
	}
}

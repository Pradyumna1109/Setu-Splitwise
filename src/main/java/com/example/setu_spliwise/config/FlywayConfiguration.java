package com.example.setu_spliwise.config;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
class FlywayConfiguration implements FlywayConfigurationCustomizer {
  private final ApplicationContext applicationContext;

  @Autowired
  public FlywayConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public void customize(FluentConfiguration configuration) {
    JavaMigration[] migrationBeans =
        applicationContext
            .getBeansOfType(JavaMigration.class)
            .values()
            .toArray(new JavaMigration[0]);
    configuration.javaMigrations(migrationBeans);
  }
}

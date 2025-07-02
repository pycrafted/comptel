2025-07-02T13:06:17.135703787Z ==> Detected a new open port HTTP:8080
2025-07-02T13:06:29.714485064Z 2025-07-02T13:06:29.713Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-07-02T13:06:30.011089496Z 2025-07-02T13:06:30.010Z  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2025-07-02T13:06:30.011317332Z 2025-07-02T13:06:30.011Z  INFO 1 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.40]
2025-07-02T13:06:32.207765279Z 2025-07-02T13:06:32.114Z  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-07-02T13:06:32.211365485Z 2025-07-02T13:06:32.210Z  INFO 1 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 48296 ms
2025-07-02T13:06:43.613694043Z 2025-07-02T13:06:43.613Z DEBUG 1 --- [           main] c.c.backend.AuthentificationFilter       : Filter 'authentificationFilter' configured for use
2025-07-02T13:06:46.61441775Z 2025-07-02T13:06:46.613Z  INFO 1 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2025-07-02T13:06:48.210959758Z 2025-07-02T13:06:48.210Z  INFO 1 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.6.13.Final
2025-07-02T13:06:49.009460492Z 2025-07-02T13:06:49.009Z  INFO 1 --- [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2025-07-02T13:06:59.012208189Z 2025-07-02T13:06:59.011Z  INFO 1 --- [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2025-07-02T13:07:00.025765891Z 2025-07-02T13:07:00.025Z  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2025-07-02T13:07:06.14843404Z 2025-07-02T13:07:06.146Z  INFO 1 --- [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@3fecb076
2025-07-02T13:07:06.219652681Z 2025-07-02T13:07:06.210Z  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2025-07-02T13:07:07.217153347Z 2025-07-02T13:07:07.216Z  WARN 1 --- [           main] org.hibernate.orm.deprecation            : HHH90000025: PostgreSQLDialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2025-07-02T13:07:07.71554926Z 2025-07-02T13:07:07.715Z  INFO 1 --- [           main] org.hibernate.orm.connections.pooling    : HHH10001005: Database info:
2025-07-02T13:07:07.715565672Z 	Database JDBC URL [Connecting through datasource 'HikariDataSource (HikariPool-1)']
2025-07-02T13:07:07.715593823Z 	Database driver: undefined/unknown
2025-07-02T13:07:07.715601694Z 	Database version: 16.9
2025-07-02T13:07:07.715606444Z 	Autocommit mode: undefined/unknown
2025-07-02T13:07:07.715611035Z 	Isolation level: undefined/unknown
2025-07-02T13:07:07.715615165Z 	Minimum pool size: undefined/unknown
2025-07-02T13:07:07.715619795Z 	Maximum pool size: undefined/unknown
2025-07-02T13:07:18.770670725Z ==> Detected a new open port HTTP:8080
2025-07-02T13:07:25.311934405Z 2025-07-02T13:07:25.311Z  INFO 1 --- [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2025-07-02T13:07:31.786949246Z 2025-07-02T13:07:31.786Z  INFO 1 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2025-07-02T13:07:44.711816213Z 2025-07-02T13:07:44.711Z  INFO 1 --- [           main] o.s.d.j.r.query.QueryEnhancerFactory     : Hibernate is in classpath; If applicable, HQL parser will be used.
2025-07-02T13:07:56.809175049Z 2025-07-02T13:07:56.808Z  INFO 1 --- [           main] r$InitializeUserDetailsManagerConfigurer : Global AuthenticationManager configured with UserDetailsService bean with name useImpl
2025-07-02T13:08:01.10881497Z 2025-07-02T13:08:01.108Z  WARN 1 --- [           main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2025-07-02T13:08:15.918096236Z 2025-07-02T13:08:15.917Z  INFO 1 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 3 endpoints beneath base path '/actuator'
2025-07-02T13:08:20.066977186Z ==> Detected a new open port HTTP:8080
2025-07-02T13:08:20.710622399Z 2025-07-02T13:08:20.710Z  INFO 1 --- [           main] o.s.v.b.OptionalValidatorFactoryBean     : Failed to set up a Bean Validation provider: jakarta.validation.NoProviderFoundException: Unable to create a Configuration, because no Jakarta Bean Validation provider could be found. Add a provider like Hibernate Validator (RI) to your classpath.
2025-07-02T13:08:33.3117673Z 2025-07-02T13:08:33.311Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2025-07-02T13:08:33.710997117Z 2025-07-02T13:08:33.710Z  INFO 1 --- [           main] com.comptel.backend.BackendApplication   : Started BackendApplication in 181.599 seconds (process running for 193.45)
2025-07-02T13:08:35.508361915Z 2025-07-02T13:08:35.507Z  INFO 1 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2025-07-02T13:08:35.508477754Z 2025-07-02T13:08:35.508Z  INFO 1 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2025-07-02T13:08:35.51062013Z 2025-07-02T13:08:35.510Z  INFO 1 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms
2025-07-02T13:08:41.815314799Z 2025-07-02T13:08:41.814Z  INFO 1 --- [           main] com.comptel.backend.DataInitializer      : ℹ️  Admin user already exists, skipping creation.
2025-07-02T13:08:41.91100228Z UserRepository est prêt : org.springframework.data.jpa.repository.support.SimpleJpaRepository@28ec9c23
2025-07-02T13:08:41.911026351Z Payment.ModePaiement possibles : [WAVE, OM, CASH]
2025-07-02T13:08:45.963911381Z ==> Your service is live 🎉
2025-07-02T13:08:45.990321088Z ==> 
2025-07-02T13:08:46.015194467Z ==> ///////////////////////////////////////////////////////////
2025-07-02T13:08:46.040111395Z ==> 
2025-07-02T13:08:46.064711053Z ==> Available at your primary URL https://comptel-backend.onrender.com
2025-07-02T13:08:46.089724032Z ==> 
2025-07-02T13:08:46.11335929Z ==> ///////////////////////////////////////////////////////////
2025-07-02T13:09:22.757281039Z ==> Detected a new open port HTTP:8080
2025-07-02T13:09:45.914743721Z 2025-07-02T13:09:45.914Z  INFO 1 --- [ionShutdownHook] o.s.b.w.e.tomcat.GracefulShutdown        : Commencing graceful shutdown. Waiting for active requests to complete
2025-07-02T13:09:45.916716043Z 2025-07-02T13:09:45.916Z  INFO 1 --- [tomcat-shutdown] o.s.b.w.e.tomcat.GracefulShutdown        : Graceful shutdown complete
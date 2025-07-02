2025-07-02T12:36:10.362992707Z 	at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:91) ~[HikariCP-5.1.0.jar!/:na]
2025-07-02T12:36:10.363008527Z 	at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:111) ~[HikariCP-5.1.0.jar!/:na]
2025-07-02T12:36:10.363012418Z 	at org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl.getConnection(DatasourceConnectionProviderImpl.java:126) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363026028Z 	at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess.obtainConnection(JdbcEnvironmentInitiator.java:467) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363028818Z 	at org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl.getIsolatedConnection(DdlTransactionIsolatorNonJtaImpl.java:46) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363031748Z 	at org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl.getIsolatedConnection(DdlTransactionIsolatorNonJtaImpl.java:39) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363034528Z 	at org.hibernate.tool.schema.internal.exec.ImprovedExtractionContextImpl.getJdbcConnection(ImprovedExtractionContextImpl.java:63) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363038308Z 	at org.hibernate.tool.schema.extract.spi.ExtractionContext.getQueryResults(ExtractionContext.java:43) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363040848Z 	at org.hibernate.tool.schema.extract.internal.SequenceInformationExtractorLegacyImpl.extractMetadata(SequenceInformationExtractorLegacyImpl.java:39) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363044768Z 	at org.hibernate.tool.schema.extract.internal.DatabaseInformationImpl.initializeSequences(DatabaseInformationImpl.java:66) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363047588Z 	at org.hibernate.tool.schema.extract.internal.DatabaseInformationImpl.<init>(DatabaseInformationImpl.java:60) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363050748Z 	at org.hibernate.tool.schema.internal.Helper.buildDatabaseInformation(Helper.java:185) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363053379Z 	at org.hibernate.tool.schema.internal.AbstractSchemaMigrator.doMigration(AbstractSchemaMigrator.java:93) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363056209Z 	at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.performDatabaseAction(SchemaManagementToolCoordinator.java:280) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363058939Z 	at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.lambda$process$5(SchemaManagementToolCoordinator.java:144) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363062479Z 	at java.base/java.util.HashMap.forEach(Unknown Source) ~[na:na]
2025-07-02T12:36:10.363065439Z 	at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.process(SchemaManagementToolCoordinator.java:141) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.363068279Z 	at org.hibernate.boot.internal.SessionFactoryObserverForSchemaExport.sessionFactoryCreated(SessionFactoryObserverForSchemaExport.java:37) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.36309106Z 	at org.hibernate.internal.SessionFactoryObserverChain.sessionFactoryCreated(SessionFactoryObserverChain.java:35) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.36309444Z 	at org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:324) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.36309717Z 	at org.hibernate.boot.internal.SessionFactoryBuilderImpl.build(SessionFactoryBuilderImpl.java:463) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.36310005Z 	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1517) ~[hibernate-core-6.6.13.Final.jar!/:6.6.13.Final]
2025-07-02T12:36:10.36310287Z 	at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:66) ~[spring-orm-6.2.6.jar!/:6.2.6]
2025-07-02T12:36:10.36311157Z 	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:390) ~[spring-orm-6.2.6.jar!/:6.2.6]
2025-07-02T12:36:10.36311487Z 	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:419) ~[spring-orm-6.2.6.jar!/:6.2.6]
2025-07-02T12:36:10.36311831Z 	... 24 common frames omitted
2025-07-02T12:36:10.3631207Z 
2025-07-02T12:36:11.932804732Z 🚀 Starting Comptel Backend...
2025-07-02T12:36:11.932996686Z ✅ Using individual database credentials
2025-07-02T12:36:12.021943629Z 🔍 Host/DB part: dpg-d1ig7lumcj7s738s5k80-a/comptel
2025-07-02T12:36:12.109101257Z ✅ Constructed JDBC URL from individual credentials
2025-07-02T12:36:12.109117977Z 🔧 Using SPRING_DATASOURCE_URL: jdbc:postgresql://comptel_user:Ft9BGWDZEInRTEGgySf8cVHAPmTAz21j@dpg-d1ig7lumcj7s738s5k80-a/comptel
2025-07-02T12:36:12.109123438Z 🎯 Starting Spring Boot application...
2025-07-02T12:36:13.121983041Z ==> Exited with status 1
2025-07-02T12:36:13.135529179Z ==> Common ways to troubleshoot your deploy: https://render.com/docs/troubleshooting-deploys
2025-07-02T12:36:36.118321482Z 
2025-07-02T12:36:36.118349942Z   .   ____          _            __ _ _
2025-07-02T12:36:36.118354953Z  /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
2025-07-02T12:36:36.118358733Z ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
2025-07-02T12:36:36.118362363Z  \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
2025-07-02T12:36:36.118407004Z   '  |____| .__|_| |_|_| |_\__, | / / / /
2025-07-02T12:36:36.118411024Z  =========|_|==============|___/=/_/_/_/
2025-07-02T12:36:36.118414674Z 
2025-07-02T12:36:36.214623784Z  :: Spring Boot ::                (v3.4.5)
2025-07-02T12:36:36.216813937Z 
2025-07-02T12:36:38.022621878Z 2025-07-02T12:36:38.014Z  INFO 1 --- [           main] com.comptel.backend.BackendApplication   : Starting BackendApplication v0.0.1-SNAPSHOT using Java 21.0.7 with PID 1 (/app/app.jar started by root in /app)
2025-07-02T12:36:38.022652029Z 2025-07-02T12:36:38.019Z DEBUG 1 --- [           main] com.comptel.backend.BackendApplication   : Running with Spring Boot v3.4.5, Spring v6.2.6
2025-07-02T12:36:38.110128076Z 2025-07-02T12:36:38.109Z  INFO 1 --- [           main] com.comptel.backend.BackendApplication   : No active profile set, falling back to 1 default profile: "default"
2025-07-02T12:37:08.114746399Z 2025-07-02T12:37:08.114Z  INFO 1 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2025-07-02T12:37:09.809823238Z 2025-07-02T12:37:09.809Z  INFO 1 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 1496 ms. Found 9 JPA repository interfaces.
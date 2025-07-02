2025-07-02T12:33:37.109569559Z Upload succeeded
2025-07-02T12:33:39.909015086Z ==> Deploying...
2025-07-02T12:33:47.31977856Z /docker-entrypoint.sh: /docker-entrypoint.d/ is not empty, will attempt to perform configuration
2025-07-02T12:33:47.319908932Z /docker-entrypoint.sh: Looking for shell scripts in /docker-entrypoint.d/
2025-07-02T12:33:47.32362705Z /docker-entrypoint.sh: Launching /docker-entrypoint.d/10-listen-on-ipv6-by-default.sh
2025-07-02T12:33:47.383582781Z 10-listen-on-ipv6-by-default.sh: info: Getting the checksum of /etc/nginx/conf.d/default.conf
2025-07-02T12:33:47.479791904Z 10-listen-on-ipv6-by-default.sh: info: /etc/nginx/conf.d/default.conf differs from the packaged version
2025-07-02T12:33:47.479922196Z /docker-entrypoint.sh: Sourcing /docker-entrypoint.d/15-local-resolvers.envsh
2025-07-02T12:33:47.480072439Z /docker-entrypoint.sh: Launching /docker-entrypoint.d/20-envsubst-on-templates.sh
2025-07-02T12:33:47.482891163Z /docker-entrypoint.sh: Launching /docker-entrypoint.d/30-tune-worker-processes.sh
2025-07-02T12:33:47.484271674Z /docker-entrypoint.sh: Configuration complete; ready for start up
2025-07-02T12:33:47.573423358Z 2025/07/02 12:33:47 [notice] 1#1: using the "epoll" event method
2025-07-02T12:33:47.573462649Z 2025/07/02 12:33:47 [notice] 1#1: nginx/1.29.0
2025-07-02T12:33:47.573466839Z 2025/07/02 12:33:47 [notice] 1#1: built by gcc 14.2.0 (Alpine 14.2.0) 
2025-07-02T12:33:47.573469279Z 2025/07/02 12:33:47 [notice] 1#1: OS: Linux 6.8.0-1029-aws
2025-07-02T12:33:47.573471379Z 2025/07/02 12:33:47 [notice] 1#1: getrlimit(RLIMIT_NOFILE): 1048576:1048576
2025-07-02T12:33:47.576600507Z 2025/07/02 12:33:47 [notice] 1#1: start worker processes
2025-07-02T12:33:47.576814231Z 2025/07/02 12:33:47 [notice] 1#1: start worker process 29
2025-07-02T12:33:47.576985464Z 2025/07/02 12:33:47 [notice] 1#1: start worker process 30
2025-07-02T12:33:47.577193117Z 2025/07/02 12:33:47 [notice] 1#1: start worker process 31
2025-07-02T12:33:47.577623063Z 2025/07/02 12:33:47 [notice] 1#1: start worker process 32
2025-07-02T12:33:47.577640014Z 2025/07/02 12:33:47 [notice] 1#1: start worker process 33
2025-07-02T12:33:47.577918928Z 2025/07/02 12:33:47 [notice] 1#1: start worker process 34
2025-07-02T12:33:47.578191342Z 2025/07/02 12:33:47 [notice] 1#1: start worker process 35
2025-07-02T12:33:47.578486597Z 2025/07/02 12:33:47 [notice] 1#1: start worker process 36
2025-07-02T12:33:50.661208343Z 10.219.24.139 - - [02/Jul/2025:12:33:50 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:33:51.454636783Z 10.219.24.139 - - [02/Jul/2025:12:33:51 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:33:51.776992119Z ==> Your service is live 🎉
2025-07-02T12:33:51.800643947Z ==> 
2025-07-02T12:33:52.03776113Z ==> ///////////////////////////////////////////////////////////
2025-07-02T12:33:52.275028593Z ==> 
2025-07-02T12:33:52.297581101Z ==> Available at your primary URL https://comptel-frontend.onrender.com
2025-07-02T12:33:52.32049924Z ==> 
2025-07-02T12:33:52.342635138Z ==> ///////////////////////////////////////////////////////////
2025-07-02T12:33:53.323074146Z 127.0.0.1 - - [02/Jul/2025:12:33:53 +0000] "GET / HTTP/1.1" 200 644 "-" "Go-http-client/2.0" "35.247.111.159, 104.23.160.51, 10.220.97.138"
2025-07-02T12:33:53.762310056Z 127.0.0.1 - - [02/Jul/2025:12:33:53 +0000] "HEAD / HTTP/1.1" 200 0 "-" "Go-http-client/1.1" "-"
2025-07-02T12:33:55.659217901Z 10.219.24.139 - - [02/Jul/2025:12:33:55 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:00.659620599Z 10.219.24.139 - - [02/Jul/2025:12:34:00 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:05.659102955Z 10.219.24.139 - - [02/Jul/2025:12:34:05 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:10.659162173Z 10.219.24.139 - - [02/Jul/2025:12:34:10 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:10.659192343Z 10.219.24.139 - - [02/Jul/2025:12:34:10 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:15.658829486Z 10.219.24.139 - - [02/Jul/2025:12:34:15 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:20.658580994Z 10.219.24.139 - - [02/Jul/2025:12:34:20 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:25.659275749Z 10.219.24.139 - - [02/Jul/2025:12:34:25 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:30.658693156Z 10.219.24.139 - - [02/Jul/2025:12:34:30 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:35.659431667Z 10.219.24.139 - - [02/Jul/2025:12:34:35 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:40.659453849Z 10.219.24.139 - - [02/Jul/2025:12:34:40 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:40.659479719Z 10.219.24.139 - - [02/Jul/2025:12:34:40 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:45.659024407Z 10.219.24.139 - - [02/Jul/2025:12:34:45 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
2025-07-02T12:34:50.658676598Z 10.219.24.139 - - [02/Jul/2025:12:34:50 +0000] "GET / HTTP/1.1" 200 644 "-" "Render/1.0" "-"
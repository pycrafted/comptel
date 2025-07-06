# 📊 Système de Monitoring Comptel

Ce document décrit la configuration et l'utilisation du système de monitoring pour l'application Comptel.

## 🏗️ Architecture

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Frontend  │    │   Backend   │    │  PostgreSQL │
│   (React)   │    │ (Spring Boot)│    │             │
└─────────────┘    └─────────────┘    └─────────────┘
                          │
                          ▼
                   ┌─────────────┐
                   │  Prometheus │
                   │ (Collecteur)│
                   └─────────────┘
                          │
                          ▼
                   ┌─────────────┐
                   │   Grafana   │
                   │ (Visualisation)│
                   └─────────────┘
```

## 🚀 Démarrage rapide

### Windows (PowerShell)
```powershell
.\scripts\start-monitoring.ps1
```

### Linux/Mac (Bash)
```bash
./scripts/start-monitoring.sh
```

### Manuel
```bash
cd docker
docker-compose up -d
```

## 📋 URLs d'accès

| Service | URL | Description |
|---------|-----|-------------|
| **Application** | http://localhost:3000 | Interface utilisateur React |
| **Backend API** | http://localhost:8080 | API Spring Boot |
| **Métriques** | http://localhost:8080/actuator/prometheus | Métriques Prometheus |
| **Prometheus** | http://localhost:9090 | Interface Prometheus |
| **Grafana** | http://localhost:3001 | Interface Grafana |

## 🔐 Identifiants Grafana

- **Utilisateur :** `admin`
- **Mot de passe :** `admin`

## 📊 Métriques disponibles

### Métriques HTTP
- **Taux de requêtes** : Nombre de requêtes par seconde
- **Temps de réponse** : Latence des endpoints (50e, 95e, 99e percentile)
- **Codes de statut** : Répartition des codes de réponse HTTP
- **Taux d'erreur** : Pourcentage de requêtes en erreur

### Métriques JVM
- **Utilisation mémoire** : Heap et non-heap
- **Garbage Collection** : Fréquence et durée des GC
- **Threads** : Nombre de threads actifs
- **CPU** : Utilisation CPU système

### Métriques Base de données
- **Connexions actives** : Nombre de connexions HikariCP
- **Connexions inactives** : Connexions en pool
- **Temps de requête** : Latence des requêtes SQL

### Métriques Métier
- **Nombre de factures** : Total, du jour, du mois
- **Montants** : Chiffre d'affaires, paiements
- **Entrées/Sorties** : Flux de trésorerie

## 🎯 Dashboard Grafana

Le dashboard "Comptel Application Dashboard" inclut :

### Panels principaux
1. **HTTP Requests Rate** - Taux de requêtes par endpoint
2. **HTTP Response Time** - Temps de réponse (50e et 95e percentile)
3. **JVM Memory Usage** - Utilisation mémoire heap
4. **Database Connections** - Connexions actives
5. **Error Rate** - Taux d'erreurs 4xx et 5xx
6. **System CPU Usage** - Utilisation CPU

### Métriques métier
- **Factures créées** par période
- **Chiffre d'affaires** en temps réel
- **Paiements reçus** vs facturé
- **Flux de trésorerie** (entrées/sorties)

## 🔧 Configuration

### Backend Spring Boot
```properties
# Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always

# Prometheus configuration
management.metrics.export.prometheus.enabled=true
management.metrics.tags.application=comptel-backend
management.metrics.distribution.percentiles-histogram.http.server.requests=true
management.metrics.distribution.percentiles.http.server.requests=0.5,0.95,0.99
```

### Prometheus
```yaml
scrape_configs:
  - job_name: 'comptel-backend'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['backend:8080']
    scrape_interval: 10s
```

### Grafana
- **Source de données** : Prometheus (http://prometheus:9090)
- **Dashboard** : Provisionnement automatique
- **Rafraîchissement** : 10 secondes

## 🛠️ Développement

### Ajouter de nouvelles métriques

1. **Métriques personnalisées** :
```java
@RestController
public class MyController {
    
    private final Counter requestCounter;
    private final Timer requestTimer;
    
    public MyController(MeterRegistry meterRegistry) {
        this.requestCounter = Counter.builder("my_requests_total")
            .description("Total requests")
            .register(meterRegistry);
        this.requestTimer = Timer.builder("my_request_duration")
            .description("Request duration")
            .register(meterRegistry);
    }
    
    @GetMapping("/my-endpoint")
    public ResponseEntity<?> myEndpoint() {
        requestCounter.increment();
        return requestTimer.record(() -> {
            // Votre logique ici
            return ResponseEntity.ok().build();
        });
    }
}
```

2. **Métriques métier** :
```java
@Component
public class BusinessMetrics {
    
    private final Gauge invoiceGauge;
    
    public BusinessMetrics(MeterRegistry meterRegistry, InvoiceRepository repo) {
        this.invoiceGauge = Gauge.builder("invoices_total")
            .description("Total number of invoices")
            .register(meterRegistry, repo, InvoiceRepository::count);
    }
}
```

### Ajouter de nouveaux dashboards

1. Créer un fichier JSON dans `docker/grafana/dashboards/`
2. Le dashboard sera automatiquement provisionné
3. Redémarrer Grafana si nécessaire

## 🚨 Alertes

### Configuration des alertes Prometheus
```yaml
# prometheus.yml
rule_files:
  - "alerts.yml"

# alerts.yml
groups:
  - name: comptel_alerts
    rules:
      - alert: HighErrorRate
        expr: rate(http_server_requests_seconds_count{status=~"5.."}[5m]) > 0.1
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "High error rate detected"
```

### Alertes Grafana
- **Mémoire élevée** : > 80% utilisation heap
- **Temps de réponse élevé** : > 2 secondes (95e percentile)
- **Taux d'erreur élevé** : > 5% requêtes en erreur

## 🔍 Dépannage

### Problèmes courants

1. **Grafana ne se connecte pas à Prometheus**
   - Vérifier que Prometheus est démarré
   - Vérifier l'URL dans la configuration Grafana
   - Vérifier les logs : `docker-compose logs grafana`

2. **Pas de métriques dans Prometheus**
   - Vérifier que le backend expose `/actuator/prometheus`
   - Vérifier la configuration Prometheus
   - Vérifier les logs : `docker-compose logs prometheus`

3. **Dashboard vide dans Grafana**
   - Vérifier que les métriques existent dans Prometheus
   - Vérifier les requêtes PromQL
   - Vérifier la source de données

### Commandes utiles

```bash
# Voir les logs
docker-compose logs -f [service]

# Redémarrer un service
docker-compose restart [service]

# Vérifier les métriques
curl http://localhost:8080/actuator/prometheus

# Vérifier Prometheus
curl http://localhost:9090/api/v1/targets

# Vérifier Grafana
curl http://localhost:3001/api/health
```

## 📈 Optimisation

### Performance
- **Scrape interval** : Ajuster selon les besoins (10s par défaut)
- **Rétention** : 200h par défaut, ajuster selon l'espace disque
- **Métriques** : Désactiver les métriques non utilisées

### Sécurité
- **Authentification** : Configurer des identifiants sécurisés
- **HTTPS** : Utiliser des certificats SSL en production
- **Réseau** : Limiter l'accès aux ports de monitoring

## 🔄 Maintenance

### Sauvegarde
```bash
# Sauvegarder les données Prometheus
docker cp comptel-prometheus:/prometheus ./backup/prometheus

# Sauvegarder les dashboards Grafana
docker cp comptel-grafana:/var/lib/grafana ./backup/grafana
```

### Mise à jour
```bash
# Mettre à jour les images
docker-compose pull
docker-compose up -d

# Vérifier la compatibilité
docker-compose logs
```

## 📚 Ressources

- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Micrometer Prometheus](https://micrometer.io/docs/registry/prometheus)
- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)
- [PromQL Reference](https://prometheus.io/docs/prometheus/latest/querying/basics/) 
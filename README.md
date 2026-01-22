# Dubito
Dubito con dadi (Perudo) in multiplayer sviluppato tramite Java RMI con dimostrazione in Docker

## Avvio del Progetto

### Prerequisiti

* Docker 
* Docker Compose

Il progetto è stato testato in ambiente WSL (distribuzioni Ubuntu e Debian)

## Configurazione

Presenza di file `.env` per configurarazione di alcuni parametri

- `MAX_PLAYER`
    numero massimo di giocatori in partita
- `LOBBY_TIMER_SECONDS`
    tempo massimo di attesa prima dell'inizio della partita se non raggiunto il massimo numero di giocatori
- `CRASH_TIMER_SECONDS`
    tempo di attesa di una mossa dopo il quale il server considera un player crashato

### Esecuzione

1. Build delle immagini Docker
```bash
docker compose build
```
2. Avvio del server
```bash
docker compose up gameserver
```
3. Avvio di un client
```bash
docker compose run --rm player
```

## Note

Questo progetto è stato sviluppato a scopo didattico per lo studio delle architetture distribuite, in particolar modo della comunicazione client-server tramite Java RMI
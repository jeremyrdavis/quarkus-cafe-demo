# quarkus-cafe-web

This is the web frontend for the Quarkus Coffeeshop Application

Orders can be placed through the web UI or a REST endpoint "/order"

## Running locally

See [Working Locally](../WORKING-LOCALLY.md) for details

### pgAdmin

The docker-compose file starts an instance of pgAdmin4.  You can login with:
* quarkus.cafe@redhat.com/redhat-20

You will need to create a connection to the Crunchy PostgreSQL database.  Use the following values:
* General 
** Name: pg10
* Connection
** Host: crunchy
** Port: 5432
** Maintenance database: postgres
** Username: postgres
** Password: redhat-20

The settings are not currently persisted across restarts so they will have to be recreated each time "docker-compose up" is run




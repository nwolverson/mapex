FROM mdillon/postgis

WORKDIR /data

RUN apt-get update && apt-get install xz-utils
COPY setup.sh /docker-entrypoint-initdb.d/setup.sh

COPY postcodes.sql.xz /data
RUN chown postgres:postgres /data/postcodes.sql.xz && chown postgres:postgres /data
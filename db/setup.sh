#!/bin/bash
set -e

whoami
ls -l /data
xz -d /data/postcodes.sql.xz && psql --username "$POSTGRES_USER" postgres -U postgres < /data/postcodes.sql

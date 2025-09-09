#!/bin/bash
# Backup all configuration files
tar -czf config-backup-$(date +%Y%m%d).tar.gz \
  docker-compose.yml \
  */src/main/resources/application.properties \
  */src/main/resources/application.yml \
  logstash/config/ \
  filebeat/
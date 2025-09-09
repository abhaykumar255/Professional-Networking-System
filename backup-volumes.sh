#!/bin/bash
# Backup database volumes
docker run --rm -v notifications-db-data:/data -v $(pwd):/backup alpine tar czf /backup/notifications-db-backup.tar.gz -C /data .
docker run --rm -v posts-db-data:/data -v $(pwd):/backup alpine tar czf /backup/posts-db-backup.tar.gz -C /data .
docker run --rm -v users-db-data:/data -v $(pwd):/backup alpine tar czf /backup/users-db-backup.tar.gz -C /data .
docker run --rm -v connections-db-data:/data -v $(pwd):/backup alpine tar czf /backup/connections-db-backup.tar.gz -C /data .
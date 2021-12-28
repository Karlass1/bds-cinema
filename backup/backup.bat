echo off
echo 'Generate backup file name'

set BACKUP_FILE=%date%.custom.backup

echo 'Backup path: %BACKUP_FILE%'
echo 'Creating a backup ...'

set PGPASSWORD=VUTBRpass2001
"C:\Program Files\PostgreSQL\14\bin\pg_dump.exe" --username="postgres" -d BDS-project --format=custom -f "%BACKUP_FILE%"

echo 'Backup successfully created: %BACKUP_FILE%'
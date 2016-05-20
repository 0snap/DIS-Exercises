-- A) Öffnen Sie eine Verbindung zur Datenbank VSISP
db2 connect vsisp user vsisp09

--Wie lautet die aktuelle Isolationsstufe?

values current isolation 
-- keine Isolation zugewiesen-- 

-- B) Tabelle OPK mit den Spalten ID und NAME 
db2 "create table OPK (ID integer, Name varchar(50))"

--einigen Beispieldaten

insert into  OPK (id, name) values (1, 'julian');
insert into  OPK (id, name) values (2, 'Felix');
insert into  OPK (id, name) values (3, 'Tobias');

--  C) Deaktivieren Sie das Auto-Commit der bestehenden Verbindung
db2options='+c'

db2 "select * from OPK where ID=1"

--Tabelle für die Aufnahme der Snapshot-Informationen
-- "-1" bedeutet, dass diese Abfrage auf der aktuellen Datenbankpartition durchgeführt wird.
db2 "create table snapshot_dyn_sql as (select * from table snapshot_dyn_sql('OPK', -1)) as temp) definition only"
db2 "describe output select * from table(snapshot_lock('OPK', -1)) as temp_tab"

-- D) Committen Sie die Transaktion und setzen Sie die Isolationsstufe 
--    für die nächste Transaktion auf RS
SET CURRENT ISOLATION TO RS 

--The following syntax is also supported:
-- - The word CURRENT is optional *
-- - The equals sign is optional — but TO can be specified in place of the equal sign (=) *
-- - DIRTY READ can be specified in place of UR *
-- - READ UNCOMMITTED can be specified in place of UR *
-- - READ COMMITTED is recognized and upgraded to CS *
-- - CURSOR STABILITY can be specified in place of CS *
-- - REPEATABLE READ can be specified in place of RR *
-- - SERIALIZABLE can be specified in place of RR 
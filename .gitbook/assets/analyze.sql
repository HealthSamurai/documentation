---- db: -h localhost -p 5460 -U postgres tealnet

drop index code;
create index code on concept ((concept.resource #>> '{code}'::text[]));

drop index code1;
create index code1 on concept ((resource#>>'{code}'));

drop index mkbCode;
create index mkbCode on triumvirate ((triumvirate.resource #>> '{mkbCode}'::text[]));
----

\timing

--drop index concept_resource_system_path;
EXPLAIN (ANALYZE,BUFFERS)

SELECT *
FROM triumvirate
join concept
on  triumvirate.resource#>>'{mkbCode}' = concept.resource#>>'{code}' 
--where concept.resource#>>'{system}' = 'icd-10-ru'
-- order by concept.resource#>>'{system}'
;

EXPLAIN (ANALYZE,BUFFERS)
select *,
(select array_agg(row_to_json(c.*)) foo from concept c
where c.resource#>>'{code}' = triumvirate.resource#>>'{mkbCode}'
) concept
from triumvirate;

----

select resource#>>'{mkbCode}'
from triumvirate;



----
drop index concept_resource_system_path;
select * from pg_indexes where tablename = 'concept';

SELECT current_setting('shared_buffers') AS shared_buffers,
       pg_size_pretty(pg_table_size('concept')) AS table_size;


----

SELECT distinct resource#>>'{system}'
FROM concept;
----

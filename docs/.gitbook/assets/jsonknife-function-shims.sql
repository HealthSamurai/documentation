CREATE OR REPLACE FUNCTION
knife_date_bound(date text, minmax text) returns timestamptz
AS $$
SELECT
CASE
  WHEN length(date) = 4
  THEN (date || CASE WHEN minmax = 'min' THEN '-01-01' ELSE '-12-31' END)::timestamptz
  WHEN length(date) = 7
  THEN CASE WHEN minmax = 'min'
        THEN (date || '-01')::timestamptz
        ELSE (date || '-01')::timestamptz + interval '1 month' - interval '1 second'
       END
  WHEN length(date) = 10
  THEN (date || CASE WHEN minmax = 'min' THEN 'T00:00:00' ELSE 'T23:59:59' END)::timestamptz
  WHEN length(date) = 11
  THEN (date || CASE WHEN minmax = 'min' THEN '00:00:00' ELSE '23:59:59' END)::timestamptz
  ELSE date::timestamptz
END
$$  LANGUAGE sql IMMUTABLE PARALLEL SAFE;

create or replace function knife_extract(resource jsonb, paths jsonb)
returns jsonb[] as $$
   select (select nullif(result, '{}'::jsonb[]) from jsonb_to_record(jsonb_build_object('result', jsonb_path_query_array(jsonb_agg(result), '$[*][*]'))) as x(result jsonb[]))
 
   from jsonb_array_elements(paths) as knife_paths(knife_path)
   cross join lateral (select concat('$',
                                     string_agg(case
                                                  when jsonb_typeof(element) = 'string'
                                                  then concat('."', element#>>'{}', '"')

                                                  when jsonb_typeof(element) = 'number'
                                                  then concat('[', element#>>'{}', ']')

                                                  when jsonb_typeof(element) = 'object'
                                                  then '? ('
                                                       || (select string_agg('@.' || m.key || ' == "' || m.value || '"', ' && ')
                                                           from jsonb_each_text(element) as m)
                                                       || ')'
                                                end,
                                                ''),
                                     '[*]')::jsonpath
                       from jsonb_array_elements(knife_path) as knife_path_els(element)) as jpaths(jpath)
  cross join lateral jsonb_path_query_array(resource, jpath) as result

$$ language sql immutable parallel safe strict;

create or replace function knife_extract_text(resource jsonb, paths jsonb)
returns text[] as $$
   select (select nullif(result, '{}'::text[]) from jsonb_to_record(jsonb_build_object('result', jsonb_path_query_array(jsonb_agg(result), '$[*][*]'))) as x(result text[]))
 
   from jsonb_array_elements(paths) as knife_paths(knife_path)
   cross join lateral (select concat('$',
                                     string_agg(case
                                                  when jsonb_typeof(element) = 'string'
                                                  then concat('."', element#>>'{}', '"')

                                                  when jsonb_typeof(element) = 'number'
                                                  then concat('[', element#>>'{}', ']')

                                                  when jsonb_typeof(element) = 'object'
                                                  then '? ('
                                                       || (select string_agg('@.' || m.key || ' == "' || m.value || '"', ' && ')
                                                           from jsonb_each_text(element) as m)
                                                       || ')'
                                                end,
                                                ''),
                                     '[*]')::jsonpath
                       from jsonb_array_elements(knife_path) as knife_path_els(element)) as jpaths(jpath)
  cross join lateral jsonb_path_query_array(resource, jpath) as result

$$ language sql immutable parallel safe strict;
;

create or replace function knife_extract_max_timestamptz(resource jsonb, paths jsonb)
returns timestamptz as $$
   select (select max(knife_date_bound(v#>>'{}', 'max')) from jsonb_array_elements(jsonb_path_query_array(jsonb_agg(result), '$[*][*]')) as r(v))
 
   from jsonb_array_elements(paths) as knife_paths(knife_path)
   cross join lateral (select concat('$',
                                     string_agg(case
                                                  when jsonb_typeof(element) = 'string'
                                                  then concat('."', element#>>'{}', '"')

                                                  when jsonb_typeof(element) = 'number'
                                                  then concat('[', element#>>'{}', ']')

                                                  when jsonb_typeof(element) = 'object'
                                                  then '? ('
                                                       || (select string_agg('@.' || m.key || ' == "' || m.value || '"', ' && ')
                                                           from jsonb_each_text(element) as m)
                                                       || ')'
                                                end,
                                                ''),
                                     '[*]')::jsonpath
                       from jsonb_array_elements(knife_path) as knife_path_els(element)) as jpaths(jpath)
  cross join lateral jsonb_path_query_array(resource, jpath) as result

$$ language sql immutable parallel safe strict;

create or replace function knife_extract_min_timestamptz(resource jsonb, paths jsonb)
returns timestamptz as $$
   select (select min(knife_date_bound(v#>>'{}', 'min')) from jsonb_array_elements(jsonb_path_query_array(jsonb_agg(result), '$[*][*]')) as r(v))
 
   from jsonb_array_elements(paths) as knife_paths(knife_path)
   cross join lateral (select concat('$',
                                     string_agg(case
                                                  when jsonb_typeof(element) = 'string'
                                                  then concat('."', element#>>'{}', '"')

                                                  when jsonb_typeof(element) = 'number'
                                                  then concat('[', element#>>'{}', ']')

                                                  when jsonb_typeof(element) = 'object'
                                                  then '? ('
                                                       || (select string_agg('@.' || m.key || ' == "' || m.value || '"', ' && ')
                                                           from jsonb_each_text(element) as m)
                                                       || ')'
                                                end,
                                                ''),
                                     '[*]')::jsonpath
                       from jsonb_array_elements(knife_path) as knife_path_els(element)) as jpaths(jpath)
  cross join lateral jsonb_path_query_array(resource, jpath) as result


$$ language sql immutable parallel safe strict;

create or replace function knife_extract_min_numeric(resource jsonb, paths jsonb)
returns numeric as $$
select (select min((x#>>'{}')::numeric) from jsonb_array_elements(jsonb_path_query_array(jsonb_agg(result), '$[*][*]')) as r(x))
 
   from jsonb_array_elements(paths) as knife_paths(knife_path)
   cross join lateral (select concat('$',
                                     string_agg(case
                                                  when jsonb_typeof(element) = 'string'
                                                  then concat('."', element#>>'{}', '"')

                                                  when jsonb_typeof(element) = 'number'
                                                  then concat('[', element#>>'{}', ']')

                                                  when jsonb_typeof(element) = 'object'
                                                  then '? ('
                                                       || (select string_agg('@.' || m.key || ' == "' || m.value || '"', ' && ')
                                                           from jsonb_each_text(element) as m)
                                                       || ')'
                                                end,
                                                ''),
                                     '[*]')::jsonpath
                       from jsonb_array_elements(knife_path) as knife_path_els(element)) as jpaths(jpath)
  cross join lateral jsonb_path_query_array(resource, jpath) as result

$$ language sql immutable parallel safe strict;

create or replace function knife_extract_max_numeric(resource jsonb, paths jsonb)
returns numeric as $$
select (select max((x#>>'{}')::numeric) from jsonb_array_elements(jsonb_path_query_array(jsonb_agg(result), '$[*][*]')) as r(x))
 
   from jsonb_array_elements(paths) as knife_paths(knife_path)
   cross join lateral (select concat('$',
                                     string_agg(case
                                                  when jsonb_typeof(element) = 'string'
                                                  then concat('."', element#>>'{}', '"')

                                                  when jsonb_typeof(element) = 'number'
                                                  then concat('[', element#>>'{}', ']')

                                                  when jsonb_typeof(element) = 'object'
                                                  then '? ('
                                                       || (select string_agg('@.' || m.key || ' == "' || m.value || '"', ' && ')
                                                           from jsonb_each_text(element) as m)
                                                       || ')'
                                                end,
                                                ''),
                                     '[*]')::jsonpath
                       from jsonb_array_elements(knife_path) as knife_path_els(element)) as jpaths(jpath)
  cross join lateral jsonb_path_query_array(resource, jpath) as result

$$ language sql immutable parallel safe strict;
;

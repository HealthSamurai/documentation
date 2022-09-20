# How to migrate transaction id to bigint

Prior to Aidbox release `edge:2209` internal transactions table was using column with `int` type for tracking transaction id. This table is used behind the scenes for History API and ETags. In case of a heavy load updating this id can lead to integer overflow. Perform the following sequence of steps so that transaction id is accomodated with higher limit integer type.

- Truncate transactions table. This step is needed so that changing column type does not take a lot of time.
```sql
TRUNCATE transaction;
```
- Change column type.
```sql
ALTER TABLE transaction ALTER COLUMN id TYPE bigint;
ALTER SEQUENCE transaction_id_seq AS bigint;
```

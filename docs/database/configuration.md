- shared buffers - 1/4 - 1/3 of ram
- vacuum scale factor to 0.1 (0.2 default) because GIN
- increase work mem to ~ 10MB (double check ram and amount of connections)
- all parallel workers (maintenance, per gather ...) - it depends of workload and hardware resources, such as CPU cores and RAM size. (need some best practices)


postgresql.conf

```conf
shared_buffers = 1GB
vacuum_scale_factor = 0.1
work_mem = 10MB
max_parallel_workers = 4
max_parallel_workers_per_gather = 2
```

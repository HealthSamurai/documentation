# OpenTelemetry Metrics

 ## Postgres 
<table><thead><tr><th width="190.3333333333333">Metric</th>
                                           <th width="160.3333333333333">Update frequency</th>
                                           <th width="160.3333333333333">Unit</th>
                                           <th width="95.3333333333333">Type</th>
                                           <th>Description</th></tr></thead>
             <tbody><tr><td><code>db.client.activity.count</code></td><td>continuous</td><td><code>1</code></td><td>gauge</td><td></td></tr><tr><td><code>db.client.blks.hit</code></td><td>continuous</td><td><code>1</code></td><td>counter</td><td></td></tr><tr><td><code>db.client.blks.read</code></td><td>continuous</td><td><code>1</code></td><td>counter</td><td></td></tr><tr><td><code>db.client.delete.total</code></td><td>continuous</td><td><code>{request}</code></td><td>counter</td><td>Total number of rows deleted from PostgreSQL</td></tr><tr><td><code>db.client.error.total</code></td><td>continuous</td><td><code>1</code></td><td>counter</td><td>Total number of PostgreSQL errors</td></tr><tr><td><code>db.client.insert.total</code></td><td>continuous</td><td><code>{request}</code></td><td>counter</td><td>Total number of rows inserted in PostgreSQL</td></tr><tr><td><code>db.client.replication.flush.lag.bytes</code></td><td>continuous</td><td><code>By</code></td><td>gauge</td><td>Number of bytes standby lags behind primary in flush phase</td></tr><tr><td><code>db.client.replication.flush.lag.ms</code></td><td>continuous</td><td><code>ms</code></td><td>gauge</td><td>Number of seconds standby lags behind primary in flush phase</td></tr><tr><td><code>db.client.replication.pending.lag.bytes</code></td><td>continuous</td><td><code>By</code></td><td>gauge</td><td>Number of bytes standby lags behind primary in pending phase</td></tr><tr><td><code>db.client.replication.replay.lag.bytes</code></td><td>continuous</td><td><code>By</code></td><td>gauge</td><td>Number of bytes standby lags behind primary in replay phase</td></tr><tr><td><code>db.client.replication.replay.lag.ms</code></td><td>continuous</td><td><code>ms</code></td><td>gauge</td><td>Number of seconds standby lags behind primary in replay phase</td></tr><tr><td><code>db.client.replication.total.lag.bytes</code></td><td>continuous</td><td><code>By</code></td><td>gauge</td><td>Number of bytes standby lags behind primary in total</td></tr><tr><td><code>db.client.replication.total.lag.ms</code></td><td>continuous</td><td><code>ms</code></td><td>gauge</td><td>Number of seconds standby lags behind primary in total</td></tr><tr><td><code>db.client.replication.write.lag.bytes</code></td><td>continuous</td><td><code>By</code></td><td>gauge</td><td>Number of bytes standby lags behind primary in write phase</td></tr><tr><td><code>db.client.replication.write.lag.ms</code></td><td>continuous</td><td><code>ms</code></td><td>gauge</td><td>Number of seconds standby lags behind primary in write phase</td></tr><tr><td><code>db.client.request.total</code></td><td>continuous</td><td><code>{request}</code></td><td>counter</td><td>Total number of PostgreSQL requests</td></tr><tr><td><code>db.client.requests.duration.sum</code></td><td>continuous</td><td><code>ms</code></td><td>counter</td><td>Requests duration sum</td></tr><tr><td><code>db.client.requests.duration.total</code></td><td>continuous</td><td><code>ms</code></td><td>counter</td><td>Requests duration total</td></tr><tr><td><code>db.client.tuples.fetch</code></td><td>continuous</td><td><code>1</code></td><td>counter</td><td>Total number of tuples fetched from PostgreSQL</td></tr><tr><td><code>db.client.tuples.return</code></td><td>continuous</td><td><code>1</code></td><td>counter</td><td>Total number of tuples returned from PostgreSQL</td></tr><tr><td><code>db.client.update.total</code></td><td>continuous</td><td><code>{request}</code></td><td>counter</td><td>Total number of rows updated in PostgreSQL</td></tr><tr><td><code>db.sql.cluster.size</code></td><td>continuous</td><td><code>By</code></td><td>gauge</td><td>Cluster size</td></tr><tr><td><code>db.client.calls.total</code></td><td>every minute</td><td><code>{request}</code></td><td>counter</td><td>Total calls</td></tr><tr><td><code>db.client.execution.time.mean</code></td><td>every minute</td><td><code>ms</code></td><td>gauge</td><td>Mean exec time</td></tr><tr><td><code>db.client.execution.time.stddev</code></td><td>every minute</td><td><code>ms</code></td><td>gauge</td><td>stddev exec time</td></tr><tr><td><code>db.sql.table.index.scan</code></td><td>every minute</td><td><code>1</code></td><td>counter</td><td>Number of index scans on a table</td></tr><tr><td><code>db.sql.table.seq.scan</code></td><td>every minute</td><td><code>1</code></td><td>counter</td><td>Number of sequential scans on a table</td></tr><tr><td><code>db.size</code></td><td>every hour</td><td><code>By</code></td><td>gauge</td><td>Database size</td></tr><tr><td><code>db.sql.connections.max</code></td><td>every hour</td><td><code>{connection}</code></td><td>gauge</td><td>max_connections</td></tr><tr><td><code>db.sql.table.size</code></td><td>every hour</td><td><code>By</code></td><td>gauge</td><td>Table size</td></tr></tbody></table>

## JVM 
<table><thead><tr><th width="190.3333333333333">Metric</th>
                                           <th width="160.3333333333333">Update frequency</th>
                                           <th width="160.3333333333333">Unit</th>
                                           <th width="95.3333333333333">Type</th>
                                           <th>Description</th></tr></thead>
             <tbody><tr><td><code>null</code></td><td>continuous</td><td><code>null</code></td><td>gauge</td><td>The approximate accumulated collection elapsed time</td></tr><tr><td><code>process.runtime.jvm.cpu.utilization</code></td><td>continuous</td><td><code>1</code></td><td>histogam</td><td>Recent CPU utilization for the whole system as reported by the JVM</td></tr><tr><td><code>process.runtime.jvm.gc.count</code></td><td>continuous</td><td><code>{count}</code></td><td>gauge</td><td>The total number of collections that have occurred</td></tr><tr><td><code>process.runtime.jvm.gc.duration</code></td><td>continuous</td><td><code>s</code></td><td>histogram</td><td>The approximate accumulated collection elapsed time</td></tr><tr><td><code>process.runtime.jvm.memory.usage</code></td><td>continuous</td><td><code>By</code></td><td>gauge</td><td>The current memory usage of non-heap memory that is used by the JVM</td></tr><tr><td><code>process.runtime.jvm.memory.usage</code></td><td>continuous</td><td><code>By</code></td><td>gauge</td><td>The current memory usage of the heap that is used for object allocation</td></tr><tr><td><code>process.runtime.jvm.system.cpu.utilization</code></td><td>continuous</td><td><code>1</code></td><td>histogam</td><td>Recent CPU utilization for the whole system as reported by the JVM</td></tr><tr><td><code>process.runtime.jvm.threads.by.state.count</code></td><td>continuous</td><td><code>{thread}</code></td><td>gauge</td><td>Number of executing platform threads by state</td></tr><tr><td><code>process.runtime.jvm.threads.count</code></td><td>continuous</td><td><code>{thread}</code></td><td>gauge</td><td>The current number of live threads</td></tr><tr><td><code>process.runtime.jvm.threads.daemon.count</code></td><td>continuous</td><td><code>{thread}</code></td><td>gauge</td><td>The current number of live daemon threads</td></tr><tr><td><code>process.runtime.jvm.threads.peak.count</code></td><td>continuous</td><td><code>{thread}</code></td><td>gauge</td><td>The peak live thread count</td></tr><tr><td><code>system.memory.utilization</code></td><td>continuous</td><td><code>1</code></td><td>gauge</td><td></td></tr><tr><td><code>jvm.core.pool.size</code></td><td>every hour</td><td><code>{thread}</code></td><td>gauge</td><td>The core number of threads in the pool</td></tr><tr><td><code>jvm.pool.size.max</code></td><td>every hour</td><td><code>{thread}</code></td><td>gauge</td><td>The maximum number of threads allowed</td></tr><tr><td><code>process.runtime.jvm.memory.size.max</code></td><td>every hour</td><td><code>By</code></td><td>gauge</td><td>The maximum amount of memory that the JVM  will attempt to use</td></tr><tr><td><code>process.runtime.jvm.memory.size.total</code></td><td>every hour</td><td><code>By</code></td><td>gauge</td><td>The total amount of memory in the JVM</td></tr><tr><td><code>process.runtime.jvm.processors.available.count</code></td><td>every hour</td><td><code>1</code></td><td>gauge</td><td>The maximum number of processors available</td></tr></tbody></table>

## HTTP 
<table><thead><tr><th width="190.3333333333333">Metric</th>
                                           <th width="160.3333333333333">Update frequency</th>
                                           <th width="160.3333333333333">Unit</th>
                                           <th width="95.3333333333333">Type</th>
                                           <th>Description</th></tr></thead>
             <tbody><tr><td><code>http.server.active_requests</code></td><td>continuous</td><td><code>{request}</code></td><td>gauge</td><td>Number of concurrent HTTP requests</td></tr><tr><td><code>http.server.queue</code></td><td>continuous</td><td><code>{request}</code></td><td>counter</td><td>Number HTTP requests completed</td></tr><tr><td><code>http.server.queue</code></td><td>continuous</td><td><code>{request}</code></td><td>gauge</td><td>Number HTTP requests in the queue</td></tr><tr><td><code>http.server.request.created</code></td><td>continuous</td><td><code>s</code></td><td>histogram</td><td></td></tr><tr><td><code>http.server.request.duration</code></td><td>continuous</td><td><code>s</code></td><td>histogram</td><td></td></tr><tr><td><code>http.server.request.used</code></td><td>continuous</td><td><code>s</code></td><td>histogram</td><td></td></tr><tr><td><code>http.server.request.wait</code></td><td>continuous</td><td><code>s</code></td><td>histogram</td><td></td></tr></tbody></table>

## Hikari (Postgres connection pool) 
<table><thead><tr><th width="190.3333333333333">Metric</th>
                                           <th width="160.3333333333333">Update frequency</th>
                                           <th width="160.3333333333333">Unit</th>
                                           <th width="95.3333333333333">Type</th>
                                           <th>Description</th></tr></thead>
             <tbody><tr><td><code>db.client.connections</code></td><td>continuous</td><td><code>{connection}</code></td><td>gauge</td><td>The number of active connections</td></tr><tr><td><code>db.client.connections.closed.total</code></td><td>continuous</td><td><code>{connection}</code></td><td>counter</td><td></td></tr><tr><td><code>db.client.connections.create_time</code></td><td>continuous</td><td><code>s</code></td><td>histogram</td><td>The time it took to create a new connection</td></tr><tr><td><code>db.client.connections.idle</code></td><td>continuous</td><td><code>{connection}</code></td><td>gauge</td><td>The number of idle connections allowed</td></tr><tr><td><code>db.client.connections.timeouts</code></td><td>continuous</td><td><code>{timeout}</code></td><td>counter</td><td></td></tr><tr><td><code>db.client.connections.total</code></td><td>continuous</td><td><code>{connection}</code></td><td>counter</td><td></td></tr><tr><td><code>db.client.connections.use_time</code></td><td>continuous</td><td><code>s</code></td><td>histogram</td><td>The time between borrowing a connection and returning it to the pool</td></tr><tr><td><code>db.client.connections.wait_time</code></td><td>continuous</td><td><code>s</code></td><td>histogram</td><td>The time it took to obtain an open connection from the pool</td></tr><tr><td><code>db.client.connections.max</code></td><td>every hour</td><td><code>{connection}</code></td><td>gauge</td><td>The maximum number of open connections allowed</td></tr></tbody></table>

# Python

This package provides a wrapper over `psycopg2` connection which provides CRUD operations for resources in Fhirbase.

## Installation

You can install the package using PyPI:

```python
pip install fhirbase
```

## Usage

Import `fhirbase` and `psycopg2` libraries:

```python
import fhirbase
import psycopg2
```

Create a connection using `psycopg2.connect`:

```python
connection = psycopg2.connect(
    dbname='postgres', user='postgres',
    host='localhost', port='5432')
```

Create an instance of `FHIRBase`:

```python
fb = fhirbase.FHIRBase(connection)
```

Now you can use the following methods of `FHIRBase` instance:

* `.execute(sql, params=None, commit=False)`
* `.execute_without_result(sql, params=None, commit=False)`
* `.row_to_resource(row)`

CRUD methods work with [FHIR resources](https://www.hl7.org/fhir/resourcelist.html). A resource represented as a dict with a specified `resourceType` key as a required key.The following methods works with a resource and returns resources.

* `.create(resource, txid=None, commit=True)`
* `.update(resource, txid=None, commit=True)`
* `.delete(resource, txid=None, commit=True) / .delete(resource_type, id, txid=None, commit=True)`
* `.read(resource)`_/_`.read(resource_type, id)` `.list(sql, params=None)`

## Methods

#### .execute <a href="#execute" id="execute"></a>

Executes sql with params.

Syntax: `.execute(sql, params=None, commit=False)`

Returns: context manager with a cursor as context

Example:

```python
with fb.execute('SELECT * FROM patient WHERE id=%s', ['id']) as cursor: 
    print(cursor.fetchall())
```

#### .execute\_without\_result <a href="#execute_without_result" id="execute_without_result"></a>

Executes sql with params.

Syntax: `.execute_without_result(sql, params=None, commit=False)`

Returns: nothing

Example:

```python
fb.execute_without_result('INSERT INTO transaction (resource) VALUES (%s)', ['{}'])
```

#### .row\_to\_resource <a href="#row_to_resource" id="row_to_resource"></a>

Transforms a raw row from DB to resource.

Syntax: `.row_to_resource(row)`

Returns: resource representation (dict)

Example:

```python
fb.row_to_resource({
    'resource': {'name': []},
    'ts': 'ts',
    'txid': 'txid',
    'resource_type': 'Patient',
    'meta': {'tag': 'created'},
    'id': 'id',
}))
```

will return a resource representation:

```python
{
    'id': 'id',
    'meta': {'lastUpdated': 'ts', 'versionId': 'txid'},
    'name': [],
    'resourceType': 'Patient',
}
```

#### .create <a href="#create" id="create"></a>

Creates a resource. If `txid` is not specified, a new unique logical transaction id will be generated.

Syntax: `.create(resource, txid=None, commit=True)`

Returns: resource representation (dict)

Example:

```python
fb.create({
    'resourceType': 'Patient',
    'name': [{'text': 'John'}],
})
```

returns:

```python
{
    'resourceType': 'Patient',
    'id': 'UNIQUE ID',
    'name': [{'text': 'John'}],
    'meta': {'lastUpdated': 'timestamp', 'versionId': 'txid'},
}
```

#### .update <a href="#update" id="update"></a>

Updates a resource. If `txid` is not specified, a new unique logical transaction id will be generated. Key `id` is required in `resource` argument.

Syntax: `.update(resource, txid=None, commit=True)`

Returns: resource representation (dict)

Example:

```python
fb.update({
    'resourceType': 'Patient',
    'id': 'id',
    'name': [{'text': 'John'}],
})
```

returns:

```python
{
    'resourceType': 'Patient',
    'id': 'UNIQUE ID',
    'name': [{'text': 'John'}],
    'meta': {'lastUpdated': 'timestamp', 'versionId': 'txid'},
}
```

#### .delete <a href="#delete" id="delete"></a>

Deletes a resource. If `txid` is not specified, a new unique logical transaction id will be generated. Keys `id` and `resourceType` are required in `resource` argument in the first variant of an usage.

Syntax: `.delete(resource, txid=None, commit=True)` or `.delete(resource_type, id, txid=None, commit=True)`

Returns: nothing

Example:

```python
fb.delete({
    'resourceType': 'Patient',
    'id': 'id',
})
```

or:

```python
fb.delete(resource_type='Patient', id='id')
```

#### .read <a href="#read" id="read"></a>

Reads a resource. Keys `id` and `resourceType` are required in `resource` argument in first variant of usage.

Syntax: `.read(resource)` or `.read(resource_type, id)`

Returns: resource representation (dict)

Example:

```python
fb.read({
    'resourceType': 'Patient',
    'id': 'id',
})
```

or:

```python
fb.read(resource_type='Patient', id='id')
```

#### .list <a href="#list" id="list"></a>

Executes SQL and returns an iterator of resources. Note: sql query must return all fields of a resource table.

Syntax: `.list(sql, params)`

Returns: iterator of resources

Example:

```python
for patient in fb.list('SELECT * FROM patient'):
    print(patient)
```

or:

```python
patients = list(fb.list('SELECT * FROM patient'))
```

## Example

To run example, just do:

```bash
git clone https://github.com/fhirbase/fhirbase.py.git
cd fhirbase.py
​
docker-compose build
docker-compose up -d
```

Wait until db starting process will be completed, and run:

```bash
docker-compose run --rm fhirbase fhirbase init 3.0.1
docker-compose run --rm fhirbasepy python examples/example.py
```

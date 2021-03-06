package com.cloudata.structured.sql.provider;

import java.util.List;
import java.util.Map;

import com.cloudata.btree.Keyspace;
import com.cloudata.structured.Keyspaces;
import com.cloudata.structured.StructuredProtocol.KeyspaceName;
import com.cloudata.structured.StructuredStore;
import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ColumnMetadata;
import com.facebook.presto.spi.ConnectorTableMetadata;
import com.facebook.presto.spi.ReadOnlyConnectorMetadata;
import com.facebook.presto.spi.SchemaTableName;
import com.facebook.presto.spi.SchemaTablePrefix;
import com.facebook.presto.spi.TableHandle;
import com.google.common.collect.Lists;

public class CloudataConnectorMetadata extends ReadOnlyConnectorMetadata {

    final String connectorId;
    private final StructuredStore store;

    public CloudataConnectorMetadata(String connectorId, StructuredStore store) {
        this.connectorId = connectorId;
        this.store = store;
    }

    @Override
    public boolean canHandle(TableHandle tableHandle) {
        return tableHandle instanceof CloudataTableHandle
                && ((CloudataTableHandle) tableHandle).getConnectorId().equals(connectorId);
    }

    @Override
    public List<String> listSchemaNames() {
        List<String> schemas = Lists.newArrayList();
        schemas.add("default");
        return schemas;
    }

    @Override
    public CloudataTableHandle getTableHandle(SchemaTableName schemaTableName) {
        String schemaName = schemaTableName.getSchemaName();
        if (!listSchemaNames().contains(schemaName)) {
            return null;
        }

        String tableName = schemaTableName.getTableName();
        KeyspaceName keyspaceName = Keyspaces.buildUserKeyspaceName(tableName);
        Integer keyspaceId = store.getKeyspaces().findKeyspaceId(keyspaceName);
        if (keyspaceId == null) {
            return null;
        }

        Keyspace keyspace = Keyspace.user(keyspaceId);
        return new CloudataTableHandle(store, connectorId, schemaName, tableName, keyspace);
    }

    @Override
    public ConnectorTableMetadata getTableMetadata(TableHandle table) {
        CloudataTableHandle tableHandle = promote(table);
        return tableHandle.getTableMetadata();
    }

    private CloudataTableHandle promote(TableHandle table) {
        assert (table != null);
        // checkArgument(table instanceof CloudataTableHandle, "tableHandle is not an instance of CloudataTableHandle");
        assert (table instanceof CloudataTableHandle);
        CloudataTableHandle tableHandle = (CloudataTableHandle) table;
        assert tableHandle.getConnectorId().equals(connectorId);
        // checkArgument(tableHandle.getConnectorId().equals(connectorId), "tableHandle is not for this connector");
        return tableHandle;
    }

    @Override
    public List<SchemaTableName> listTables(String schemaNameOrNull) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColumnHandle getColumnHandle(TableHandle tableHandle, String columnName) {
        CloudataTableHandle exampleTableHandle = promote(tableHandle);

        // ExampleTable table = exampleClient.getTable(exampleTableHandle.getSchemaName(),
        // exampleTableHandle.getTableName());
        // if (table == null) {
        // throw new TableNotFoundException(exampleTableHandle.toSchemaTableName());
        // }

        // ImmutableMap.Builder<String, ColumnHandle> columnHandles = ImmutableMap.builder();
        // for (ColumnMetadata columnMetadata : table.getColumnsMetadata()) {
        // columnHandles.put(columnMetadata.getName(), new ExampleColumnHandle(connectorId, columnMetadata));
        // }
        // return columnHandles.build();

        return exampleTableHandle.getColumnHandle(columnName);
    }

    @Override
    public Map<String, ColumnHandle> getColumnHandles(TableHandle tableHandle) {
        CloudataTableHandle exampleTableHandle = promote(tableHandle);

        // ExampleTable table = exampleClient.getTable(exampleTableHandle.getSchemaName(),
        // exampleTableHandle.getTableName());
        // if (table == null) {
        // throw new TableNotFoundException(exampleTableHandle.toSchemaTableName());
        // }

        // ImmutableMap.Builder<String, ColumnHandle> columnHandles = ImmutableMap.builder();
        // for (ColumnMetadata columnMetadata : table.getColumnsMetadata()) {
        // columnHandles.put(columnMetadata.getName(), new ExampleColumnHandle(connectorId, columnMetadata));
        // }
        // return columnHandles.build();

        return exampleTableHandle.getColumnHandles();
    }

    @Override
    public ColumnMetadata getColumnMetadata(TableHandle tableHandle, ColumnHandle column) {
        // checkNotNull(tableHandle, "tableHandle is null");
        // checkArgument(tableHandle instanceof MockTableHandle, "tableHandle is not an instance of MockTableHandle");
        // checkArgument(((MockTableHandle) tableHandle).getConnectorId().equals(connectorId),
        // "tableHandle is not for this connector");

        CloudataColumnHandle columnHandle = promote(column);

        return columnHandle.getColumnMetadata();
    }

    private CloudataColumnHandle promote(ColumnHandle column) {
        // checkNotNull(columnHandle, "columnHandle is null");
        assert column != null;
        // checkArgument(columnHandle instanceof CloudataColumnHandle,
        // "columnHandle is not an instance of CloudataColumnHandle");
        assert column instanceof CloudataColumnHandle;
        return (CloudataColumnHandle) column;
    }

    @Override
    public Map<SchemaTableName, List<ColumnMetadata>> listTableColumns(SchemaTablePrefix prefix) {
        throw new UnsupportedOperationException();
    }

}

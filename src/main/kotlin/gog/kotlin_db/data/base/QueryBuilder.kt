package gog.kotlin_db.data.base

import gog.kotlin_db.data.base.Column.QueryBuilder_ModelColumns
import gog.kotlin_db.data.base.Where.IQueryBuilder_ModelWhereNode
import gog.kotlin_db.data.base.Where.QueryBuilder_ModelWhere
import gog.kotlin_db.data.base.Where.QueryBuilder_ModelWhereGroup
import gog.kotlin_db.data.base.Where.QueryBuilder_ModelWhereGroupBuilder
import kotlin.reflect.KProperty



open class QueryBuilder(
    var queryType: String = QueryBuilder._QUERY_TYPE_FULL
) {


    companion object {
        const val _QUERY_TYPE_FULL = "full"
        const val _QUERY_TYPE_WHERE = "where"

        const val _LOGICAL_AND = "and"
        const val _LOGICAL_OR  = "or"
        const val _LOGICAL_NOT = "not"

        const val _OPERATION_EQUALS = "="
        const val _OPERATION_NOT_EQUALS = "!="
        const val _OPERATION_GEATER_THAN = ">"
        const val _OPERATION_LESS_THAN = "<"
        const val _OPERATION_GEATER_OR_EQUAL_THAN = ">="
        const val _OPERATION_LESS_OR_EQUAL_THAN = "<="
        const val _OPERATION_LIKE = "like"
        const val _OPERATION_IN = "IN"
        const val _OPERATION_BETWEEN = "between"
    }


    var tableName: String? = null;

    private val _TAG_TEMP_SELECT : String = "{{select_column}}";
    private val _TAG_TEMP_WHERE : String = "{{where_column}}";


    protected var selectColumns: MutableList<QueryBuilder_ModelColumns<*>> = ArrayList()
    protected var whereColumns: MutableList<IQueryBuilder_ModelWhereNode> = mutableListOf()


    fun setTableName(tableName: String?): QueryBuilder {
        this.tableName = tableName;
        return this;
    }



    fun <T> addSelect(column: KProperty<T> , tableName: String? = this.tableName  , alias: String?=null): QueryBuilder {
        selectColumns.add(
            QueryBuilder_ModelColumns<T>(
                name = column.name,
                alias = alias,
                tableName = tableName
            )
        )
        return this
    }








   // fun addWhereGroup(block: (QueryBuilder_ModelWhereGroupBuilder) -> String , whereLogical: String = _LOGICAL_AND ,): QueryBuilder {
    fun addWhereGroup(block: (QueryBuilder) -> String, whereLogical: String = _LOGICAL_AND,): QueryBuilder {
//        val builder = QueryBuilder_ModelWhereGroupBuilder()
//        block(builder)

        //whereColumns.add(QueryBuilder_ModelWhereGroup (whereLogical, builder.whereColumns))
        whereColumns.add(QueryBuilder_ModelWhereGroup (whereLogical, block(QueryBuilder(QueryBuilder._QUERY_TYPE_WHERE))))

        return this
    }



    fun <T> addWhere(whereKey: KProperty<T>, block: (QueryBuilder) -> String, whereOperation: String = _OPERATION_EQUALS): QueryBuilder {
        whereColumns.add(
            QueryBuilder_ModelWhere(
                whereKey = whereKey.name,
                whereValue = block(QueryBuilder()),
                whereOperation = whereOperation
            )
        )
        return this;
    }

    fun <T> addWhere(whereKey: KProperty<T> , whereValue: String , whereOperation: String = _OPERATION_EQUALS): QueryBuilder {
        whereColumns.add(
            QueryBuilder_ModelWhere(
                whereKey = whereKey.name,
                whereValue = whereValue,
                whereOperation = whereOperation
            )
        )
        return this;
    }







    fun <T> addWhereOr(whereKey: KProperty<T> , whereValue: String, whereOperation: String = _OPERATION_EQUALS ): QueryBuilder {
        whereColumns.add(
            QueryBuilder_ModelWhere(
                whereKey = whereKey.name,
                whereValue = whereValue,
                whereOperation = whereOperation,
                whereLogical = _LOGICAL_OR
            )
        )
        return this;
    }

    fun <T> addWhereOr(whereKey: KProperty<T> , block: (QueryBuilder) -> String, whereOperation: String = _OPERATION_EQUALS ): QueryBuilder {
        whereColumns.add(
            QueryBuilder_ModelWhere(
                whereKey = whereKey.name,
                whereValue = block(QueryBuilder()),
                whereOperation = whereOperation,
                whereLogical = _LOGICAL_OR
            )
        )
        return this;
    }









    fun toGroup() : String{
        return "(${this.toSql()})"
    }


    fun toSql() : String{

        when (queryType) {
            QueryBuilder._QUERY_TYPE_FULL -> {
                var sql = " select $_TAG_TEMP_SELECT from $tableName $_TAG_TEMP_WHERE ";
                sql = this.readyTempSelect(sql);
                sql = this.readyTempWhere(sql);
                return sql;
            }
            QueryBuilder._QUERY_TYPE_WHERE -> {
                var sql = "$_TAG_TEMP_WHERE ";
                sql = this.readyTempWhere(sql);
                return sql;
            }
        }

        return "";
    }


    protected fun readyTempSelect(sql: String) : String{
        var select = "";
        for ((index, column) in selectColumns.withIndex()){
            select +=  " ${column.toStr()}";
            if (index < selectColumns.size - 1){
                select += ","
            }
        }
        return sql.replace(_TAG_TEMP_SELECT, select);
    }

    protected fun readyTempWhere(sql: String) : String{
        var where = "";
        if (queryType == QueryBuilder._QUERY_TYPE_FULL && whereColumns.size > 0){
            where = " where ";
        }
        for ((index, itemWhere) in whereColumns.withIndex()){
            where +=  " ${itemWhere.toStr(index==0)}";
        }
        return sql.replace(_TAG_TEMP_WHERE, where)
        ;
    }


}
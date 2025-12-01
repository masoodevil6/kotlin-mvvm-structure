package gog.kotlin_db.data.base.Column

class QueryBuilder_ModelColumns<T>(
    val name: String,
    var tableName: String? = null,
    var alias: String? = null,
     var value: T? = null
) {

    init {
        if (tableName == null){
            tableName = ""
        }
        if (alias == null){
            alias = name
        }
    }

    fun toStr(): String {
        return " ${tableName}.${name} as ${alias} "
    }

}
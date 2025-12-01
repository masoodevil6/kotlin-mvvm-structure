package gog.kotlin_db.data

import gog.kotlin_db.data.base.DataBase
import gog.kotlin_db.data.base.QueryBuilder
import gog.kotlin_db.data.models.user.Users
import java.util.logging.LogManager

fun main() {
    LogManager.getLogManager().reset()

    val db = DataBase()
    println("Connected: ${db.connection?.isValid(2)}")





    var query =
        Users()
        //QueryBuilder(Users.tableName)
        .addSelect(Users::id)
        .addSelect(Users::name)
        .addSelect(Users::family)
        .addSelect(Users::age)
        .addWhereGroup({
            queryGroup ->
            queryGroup.addWhere(Users::id , {
                queryWhere->
                queryWhere.setTableName(Users.tableName)
                    .addSelect(Users::id)
                    .addWhere(Users::id , "1")
                    .toSql()
            }).toSql()
        })
        .addWhereGroup({
            query ->
            query.addWhere(Users::id , 2.toString()).toSql()
        })
        .addWhere(Users::id , 2.toString())
        .addWhere(Users::id , {
            query ->
            query.setTableName(Users.tableName)
                .addSelect(Users::id)
                .addWhere(Users::id , "1")
                .toSql()
        })
     /*   .addWhereGroup(QueryBuilder._LOGICAL_AND){ query ->
            query
                .addOrWhere(Users::name , "2")
                .addWhere(Users::id , "1")
        }
        .addWhereGroup(QueryBuilder._LOGICAL_AND){ query ->
            query
                .addSelect(Users::id)
                .setTableName("asd")
                .addWhere(Users::id , "1")
                .build()
        }*/
      //
        .toSql()

    println(query)

    //val users = Users();
    //users.addSelect(users::id.name , users::name.name)


   // println("selected2: ${ users::id.name}")
}
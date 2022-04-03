package sk.kasper.base.logger

object Logger {

    var e: (msg: String, e: Exception?) -> Unit = { msg, e -> }

    var d: (msg: String) -> Unit = { msg -> }

}
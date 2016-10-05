
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import tornadofx.*
import java.sql.DriverManager

class MyApp: App(MyView::class)

class MyView : View("Form") {

    private var username: TextField by singleAssign()
    private var password: TextField by singleAssign()
    private var sqlLabel: Label by singleAssign()

    override val root = borderpane {
        title = "SQL Injection Demo"

        center = form {
            fieldset {
                field("USERNAME") {
                    username = textfield()
                }
            }
            fieldset {
                field("PASSWORD") {
                    password = textfield()
                }
            }
            button("LOGIN") {
                setOnAction {
                    if (validateUser(username.text, password.text)) {
                        Alert(Alert.AlertType.INFORMATION, "Hello ${username.text}!").show()
                    } else {
                        Alert(Alert.AlertType.ERROR, "Username/Password is incorrect").show()
                    }
                }
            }
        }

        right = ImageView(Image("sql_injection.png"))

        bottom = label {
            sqlLabel = this
        }
    }

    fun validateUser(username: String, password: String): Boolean {
        val statement = db.createStatement()

        val sql = "SELECT COUNT(*) FROM USER WHERE USERNAME = '$username' AND PASSWORD = '$password'"
        sqlLabel.text = sql

        println(sql)

        val rs = statement.executeQuery(sql)
        rs.next()

        val result = rs.getInt(1) > 0
        statement.close()
        return result
    }
}

val db = DriverManager.getConnection("jdbc:sqlite::memory:").apply {
    createStatement().apply {
        execute("CREATE TABLE USER (ID INTEGER PRIMARY KEY, USERNAME VARCHAR(30) NOT NULL, PASSWORD VARCHAR(30) NOT NULL)")
        execute("INSERT INTO USER (USERNAME,PASSWORD) VALUES ('thomasnield','password123')")
        execute("INSERT INTO USER (USERNAME,PASSWORD) VALUES ('bobmarshal','batman43')")
        close()
    }
}




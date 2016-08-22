package com.pengelkes.material_fx

import com.pengelkes.material_fx.Material.Companion.flatButton
import com.pengelkes.material_fx.Material.Companion.raisedButton
import com.pengelkes.material_fx.MaterialColors.*
import com.sun.javafx.scene.control.skin.ButtonSkin
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.*
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import org.controlsfx.control.Notifications
import tornadofx.*
import java.time.LocalDate

/**
 * Created by pengelkes on 22.08.2016.
 */
class Material() : Stylesheet() {

    companion object {
        val raisedButton by cssclass()
        val flatButton by cssclass()
        val zip by cssclass()
    }

    init {
        val flat = mixin {
            backgroundInsets += box(0.px)
            focusColor = TEAL.color()
        }

        raisedButton {
            +flat
            skin = MaterialButtonSkin::class
            backgroundColor += TEAL.color()
            prefHeight = 36.px
            borderRadius += box(2.px)
            padding = box(0.px, 16.px)

            add(hover) {
                backgroundColor += TEAL_LIGHTEN_1.color()
                val dropShadow = DropShadow()
                dropShadow.offsetY = 5.0
                dropShadow.color = Color.color(0.4, 0.5, 0.5, 0.5)
                effect = dropShadow
            }

            add(pressed) {
                backgroundColor += TEAL_LIGHTEN_2.color()
            }

            add(disabled) {
                backgroundColor += GREY.color()
            }
        }

        flatButton {
            +flat
            skin = MaterialButtonSkin::class
            backgroundColor += WHITE.color()
            prefHeight = 36.px
            borderRadius += box(2.px)
            padding = box(0.px, 16.px)

            add(hover) {
                backgroundColor += FLAT_HOVERED_LIGHT.color()
            }

            add(pressed) {
                backgroundColor += FLAT_PRESSED_LIGHT.color()
            }

            add(disabled) {
                textFill = FLAT_DISABLED_TEXT_LIGHT.color()
            }
        }

        textField {
            +flat
            borderRadius += box(2.px)
            borderInsets += box(0.px, 0.px, 1.px, 0.px)
            borderColor += box(WHITE.color(), WHITE.color(), WHITE.color(), WHITE.color())
            prefHeight = 36.px

            add(focused) {
                borderColor += box(WHITE.color(), WHITE.color(), TEAL.color(), WHITE.color())
            }
        }

        comboBox {
            +flat
            borderRadius += box(2.px)
            prefHeight = 28.px

            comboBoxPopup {
                listCell {
                    add(hover) {
                        backgroundColor += TEAL.color()
                    }

                    add(selected and filled) {
                        backgroundColor += TEAL_LIGHTEN_1.color()

                        add(hover) {
                            backgroundColor += TEAL.color()
                        }
                    }
                }
            }
        }

        checkBox {
            +flat
            borderRadius += box(2.px)
            prefHeight = 28.px
        }

        datePicker {
            +flat

            datePickerPopup {
                add(hover) {
                    backgroundColor += TEAL.color()
                }

                spinner {
                    button {
                        add(hover) {
                            backgroundColor += TEAL.color()
                        }
                        add(focused) {
                            backgroundColor += TEAL.color()
                        }
                    }
                }
            }
        }

        zip {
            maxWidth = 60.px
            minWidth = maxWidth
        }
    }
}

class MaterialButtonSkin(button: Button) : ButtonSkin(button) {
    init {
        button.text = button.text.toUpperCase()
    }
}

class CustomerForm : View() {
    override val root = Form()

    val customer = Customer()

    init {
        title = "Register Customer"

        with (root) {
            fieldset("Personal Information", FontAwesomeIconView(USER)) {
                field("Name") {
                    textfield().bind(customer.nameProperty())
                }

                field("Birthday") {
                    datepicker().bind(customer.birthdayProperty())
                }
            }

            fieldset("Address", FontAwesomeIconView(HOME)) {
                field("Street") {
                    textfield().bind(customer.streetProperty())
                }
                field("Zip / City") {
                    textfield() {
                        addClass(Material.zip)
                        bind(customer.zipProperty())
                    }
                    textfield().bind(customer.cityProperty())
                }
            }

            button("Save") {
                addClass(raisedButton)
                setOnAction {
                    Notifications.create()
                            .title("Customer saved!")
                            .text("${customer.name} was born ${customer.birthday}\nand lives in\n${customer.street}, ${customer.zip} ${customer.city}")
                            .owner(this)
                            .showInformation()
                }

                // Save button is disabled until every field has a value
                disableProperty().bind(customer.nameProperty().isNull.or(customer.birthdayProperty().isNull)
                        .or(customer.streetProperty().isNull).or(customer.zipProperty().isNull)
                        .or(customer.cityProperty().isNull))
            }
        }
    }
}

class Customer {
    var name by property<String>()
    fun nameProperty() = getProperty(Customer::name)

    var birthday by property<LocalDate>()
    fun birthdayProperty() = getProperty(Customer::birthday)

    var street by property<String>()
    fun streetProperty() = getProperty(Customer::street)

    var zip by property<String>()
    fun zipProperty() = getProperty(Customer::zip)

    var city by property<String>()
    fun cityProperty() = getProperty(Customer::city)

    override fun toString() = name
}

class TestView : View("com.pengelkes.material_fx.Material UI") {
    override val root = vbox {
        style {
            padding = box(10.px)
        }

        hbox {
            style {
                padding = box(10.px)
            }

            button("Click ME") {
                addClass(raisedButton)
            }
            button("I am disabled") {
                addClass(raisedButton)
                isDisable = true
            }
        }

        hbox {
            style {
                padding = box(10.px)
            }
            button("Click me") {
                addClass(flatButton)
            }
            button("I am disabled") {
                addClass(flatButton)
                isDisable = true
            }
        }

        hbox {
            style {
                padding = box(10.px)
            }
            textfield {
                promptText = "First name"
            }
        }

        hbox {
            style {
                padding = box(10.px)
            }

            val texasCities = FXCollections.observableArrayList("Austin",
                    "Dallas", "Midland", "San Antonio", "Fort Worth")
            combobox<String> {
                items = texasCities
            }
        }

        hbox {
            style {
                padding = box(10.px)
            }

            checkbox("I am a CheckBox")
        }
    }
}
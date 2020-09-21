package com.redhat.demos.quarkus.coffeeshop.inventory.domain;

import com.redhat.quarkus.cafe.domain.Item;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.StringJoiner;

@RegisterForReflection
public class RestockBaristaCommand extends RestockItemCommand implements CoffeeshopCommand{

    Item item;

    int quantity;

    public final CommandType commandType = CommandType.RESTOCK_BARISTA_COMMAND;

    public RestockBaristaCommand() {
    }

    public RestockBaristaCommand(Item item) {
        this.item = item;
        this.quantity = 0;
    }

    public RestockBaristaCommand(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RestockBaristaCommand.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .add("quantity=" + quantity)
                .add("commandType=" + commandType)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RestockBaristaCommand that = (RestockBaristaCommand) o;

        return new EqualsBuilder()
                .append(quantity, that.quantity)
                .append(item, that.item)
                .append(commandType, that.commandType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(item)
                .append(quantity)
                .append(commandType)
                .toHashCode();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}

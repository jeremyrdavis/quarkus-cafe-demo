package com.redhat.demos.quarkus.coffeeshop.inventory.domain;

import com.redhat.quarkus.cafe.domain.Item;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.StringJoiner;

@RegisterForReflection
public class RestockKitchenCommand implements CoffeeshopCommand{

    public final CommandType commandType = CommandType.RESTOCK_KITCHEN_COMMAND;

    Item item;

    int quantity;

    public RestockKitchenCommand() {
    }

    public RestockKitchenCommand(Item item) {
        this.item = item;
        this.quantity = 0;
    }

    public RestockKitchenCommand(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RestockKitchenCommand.class.getSimpleName() + "[", "]")
                .add("commandType=" + commandType)
                .add("item=" + item)
                .add("quantity=" + quantity)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RestockKitchenCommand that = (RestockKitchenCommand) o;

        return new EqualsBuilder()
                .append(quantity, that.quantity)
                .append(commandType, that.commandType)
                .append(item, that.item)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(commandType)
                .append(item)
                .append(quantity)
                .toHashCode();
    }

    public CommandType getCommandType() {
        return commandType;
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
}

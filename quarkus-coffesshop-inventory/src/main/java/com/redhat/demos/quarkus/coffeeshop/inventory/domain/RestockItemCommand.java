package com.redhat.demos.quarkus.coffeeshop.inventory.domain;

import com.redhat.quarkus.cafe.domain.Item;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.StringJoiner;

@RegisterForReflection
public class RestockItemCommand implements CoffeeshopCommand{

    Item item;

    int quantity;

    public CommandType commandType;

    /**
     * Default empty constructor
     */
    public RestockItemCommand() {
    }

    /**
     * Constructor for use when an item is 86'd
     *
     * @param item Item
     */
    public RestockItemCommand(Item item){
        this.item = item;
        this.quantity = 0;
    }

    /**
     *
     * @param item Item
     * @param quantity int
     */
    public RestockItemCommand(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public RestockItemCommand(Item item, int quantity, CommandType commandType) {
        this.item = item;
        this.quantity = quantity;
        this.commandType = commandType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RestockItemCommand.class.getSimpleName() + "[", "]")
                .add("item=" + item)
                .add("quantity=" + quantity)
                .add("commandType=" + commandType)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RestockItemCommand that = (RestockItemCommand) o;

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
}

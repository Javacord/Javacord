package org.javacord.core.entity.message.component.internal;

import org.javacord.api.entity.message.component.SelectMenuDefaultValue;
import org.javacord.api.entity.message.component.SelectMenuDefaultValueType;
import org.javacord.api.entity.message.component.internal.SelectMenuDefaultValueBuilderDelegate;
import org.javacord.core.entity.message.component.SelectMenuDefaultValueImpl;

public class SelectMenuDefaultValueBuilderDelegateImpl implements SelectMenuDefaultValueBuilderDelegate {

    private long id = 0L;

    private SelectMenuDefaultValueType type = null;

    @Override
    public void copy(SelectMenuDefaultValue selectMenuDefaultValue) {
        id = selectMenuDefaultValue.getId();
        type = selectMenuDefaultValue.getType();
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setType(SelectMenuDefaultValueType type) {
        this.type = type;
    }

    @Override
    public SelectMenuDefaultValue build() {
        return new SelectMenuDefaultValueImpl(id, type);
    }
}

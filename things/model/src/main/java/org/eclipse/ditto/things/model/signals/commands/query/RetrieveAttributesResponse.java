/*
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.things.model.signals.commands.query;

import static org.eclipse.ditto.base.model.common.ConditionChecker.checkNotNull;

import java.util.Objects;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.base.model.common.HttpStatus;
import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.base.model.json.FieldType;
import org.eclipse.ditto.base.model.json.JsonParsableCommandResponse;
import org.eclipse.ditto.base.model.json.JsonSchemaVersion;
import org.eclipse.ditto.base.model.signals.commands.AbstractCommandResponse;
import org.eclipse.ditto.base.model.signals.commands.CommandResponseJsonDeserializer;
import org.eclipse.ditto.json.JsonField;
import org.eclipse.ditto.json.JsonFieldDefinition;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.json.JsonObjectBuilder;
import org.eclipse.ditto.json.JsonPointer;
import org.eclipse.ditto.json.JsonValue;
import org.eclipse.ditto.things.model.Attributes;
import org.eclipse.ditto.things.model.ThingId;
import org.eclipse.ditto.things.model.ThingsModelFactory;
import org.eclipse.ditto.things.model.signals.commands.ThingCommandResponse;

/**
 * Response to a {@link RetrieveAttributes} command.
 */
@Immutable
@JsonParsableCommandResponse(type = RetrieveAttributesResponse.TYPE)
public final class RetrieveAttributesResponse extends AbstractCommandResponse<RetrieveAttributesResponse>
        implements ThingQueryCommandResponse<RetrieveAttributesResponse> {

    /**
     * Type of this response.
     */
    public static final String TYPE = TYPE_PREFIX + RetrieveAttributes.NAME;

    static final JsonFieldDefinition<JsonObject> JSON_ATTRIBUTES =
            JsonFieldDefinition.ofJsonObject("attributes", FieldType.REGULAR, JsonSchemaVersion.V_2);

    private static final HttpStatus HTTP_STATUS = HttpStatus.OK;

    private static final CommandResponseJsonDeserializer<RetrieveAttributesResponse> JSON_DESERIALIZER =
            CommandResponseJsonDeserializer.newInstance(TYPE,
                    HTTP_STATUS,
                    context -> {
                        final JsonObject jsonObject = context.getJsonObject();
                        return new RetrieveAttributesResponse(
                                ThingId.of(jsonObject.getValueOrThrow(ThingCommandResponse.JsonFields.JSON_THING_ID)),
                                ThingsModelFactory.newAttributes(jsonObject.getValueOrThrow(JSON_ATTRIBUTES)),
                                context.getDeserializedHttpStatus(),
                                context.getDittoHeaders()
                        );
                    });

    private final ThingId thingId;
    private final Attributes attributes;

    private RetrieveAttributesResponse(final ThingId thingId,
            final Attributes attributes,
            final HttpStatus httpStatus,
            final DittoHeaders dittoHeaders) {

        super(TYPE, httpStatus, dittoHeaders);
        this.thingId = checkNotNull(thingId, "thingId");
        this.attributes = checkNotNull(attributes, "attributes");
    }

    /**
     * Creates a response to a {@link RetrieveAttributes} command.
     *
     * @param thingId the Thing ID of the retrieved attributes.
     * @param attributes the retrieved Attributes.
     * @param dittoHeaders the headers of the preceding command.
     * @return the response.
     * @throws NullPointerException if any argument is {@code null}.
     */
    public static RetrieveAttributesResponse of(final ThingId thingId,
            final Attributes attributes,
            final DittoHeaders dittoHeaders) {

        return new RetrieveAttributesResponse(thingId, attributes, HTTP_STATUS, dittoHeaders);
    }

    /**
     * Creates a response to a {@link RetrieveAttributes} command.
     *
     * @param thingId the Thing ID of the retrieved attributes.
     * @param jsonObject the retrieved Attributes.
     * @param dittoHeaders the headers of the preceding command.
     * @return the response.
     * @throws NullPointerException if any argument is {@code null}.
     */
    public static RetrieveAttributesResponse of(final ThingId thingId,
            @Nullable final JsonObject jsonObject,
            final DittoHeaders dittoHeaders) {

        final Attributes attributes = null != jsonObject
                ? ThingsModelFactory.newAttributes(jsonObject)
                : ThingsModelFactory.nullAttributes();

        return of(thingId, attributes, dittoHeaders);
    }

    /**
     * Creates a response to a {@link RetrieveAttributes} command from a JSON string.
     *
     * @param jsonString the JSON string of which the response is to be created.
     * @param dittoHeaders the headers of the preceding command.
     * @return the response.
     * @throws NullPointerException if {@code jsonString} is {@code null}.
     * @throws IllegalArgumentException if {@code jsonString} is empty.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonString} was not in the expected
     * format.
     */
    public static RetrieveAttributesResponse fromJson(final String jsonString, final DittoHeaders dittoHeaders) {
        return fromJson(JsonObject.of(jsonString), dittoHeaders);
    }

    /**
     * Creates a response to a {@link RetrieveAttributes} command from a JSON object.
     *
     * @param jsonObject the JSON object of which the response is to be created.
     * @param dittoHeaders the headers of the preceding command.
     * @return the response.
     * @throws NullPointerException if {@code jsonObject} is {@code null}.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonObject} was not in the expected
     * format.
     */
    public static RetrieveAttributesResponse fromJson(final JsonObject jsonObject, final DittoHeaders dittoHeaders) {
        return JSON_DESERIALIZER.deserialize(jsonObject, dittoHeaders);
    }

    @Override
    public ThingId getEntityId() {
        return thingId;
    }

    /**
     * Returns the retrieved Attributes.
     *
     * @return the retrieved Attributes.
     */
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public JsonValue getEntity(final JsonSchemaVersion schemaVersion) {
        return attributes.toJson(schemaVersion);
    }

    @Override
    public RetrieveAttributesResponse setEntity(final JsonValue entity) {
        checkNotNull(entity, "entity");
        return of(thingId, entity.asObject(), getDittoHeaders());
    }

    @Override
    public RetrieveAttributesResponse setDittoHeaders(final DittoHeaders dittoHeaders) {
        return of(thingId, attributes, dittoHeaders);
    }

    @Override
    public JsonPointer getResourcePath() {
        return JsonPointer.of("/attributes");
    }

    @Override
    protected void appendPayload(final JsonObjectBuilder jsonObjectBuilder,
            final JsonSchemaVersion schemaVersion,
            final Predicate<JsonField> thePredicate) {

        final Predicate<JsonField> predicate = schemaVersion.and(thePredicate);
        jsonObjectBuilder.set(ThingCommandResponse.JsonFields.JSON_THING_ID, thingId.toString(), predicate);
        jsonObjectBuilder.set(JSON_ATTRIBUTES, attributes, predicate);
    }

    @Override
    protected boolean canEqual(@Nullable final Object other) {
        return other instanceof RetrieveAttributesResponse;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RetrieveAttributesResponse that = (RetrieveAttributesResponse) o;
        return that.canEqual(this) &&
                Objects.equals(thingId, that.thingId) &&
                Objects.equals(attributes, that.attributes) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), thingId, attributes);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + super.toString() + ", thingId=" + thingId + ", attributes=" +
                attributes + "]";
    }

}

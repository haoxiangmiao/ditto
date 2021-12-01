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
package org.eclipse.ditto.things.model.signals.commands.modify;

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
import org.eclipse.ditto.things.model.ThingId;
import org.eclipse.ditto.things.model.signals.commands.ThingCommandResponse;

/**
 * Response to a {@link DeleteFeatureDefinition} command.
 */
@Immutable
@JsonParsableCommandResponse(type = DeleteFeatureDefinitionResponse.TYPE)
public final class DeleteFeatureDefinitionResponse extends AbstractCommandResponse<DeleteFeatureDefinitionResponse>
        implements ThingModifyCommandResponse<DeleteFeatureDefinitionResponse> {

    /**
     * Type of this response.
     */
    public static final String TYPE = TYPE_PREFIX + DeleteFeatureDefinition.NAME;

    static final JsonFieldDefinition<String> JSON_FEATURE_ID =
            JsonFieldDefinition.ofString("featureId", FieldType.REGULAR, JsonSchemaVersion.V_2);

    private static final HttpStatus HTTP_STATUS = HttpStatus.NO_CONTENT;

    private static final CommandResponseJsonDeserializer<DeleteFeatureDefinitionResponse> JSON_DESERIALIZER =
            CommandResponseJsonDeserializer.newInstance(TYPE,
                    HTTP_STATUS,
                    context -> {
                        final JsonObject jsonObject = context.getJsonObject();
                        return new DeleteFeatureDefinitionResponse(
                                ThingId.of(jsonObject.getValueOrThrow(ThingCommandResponse.JsonFields.JSON_THING_ID)),
                                jsonObject.getValueOrThrow(JSON_FEATURE_ID),
                                context.getDeserializedHttpStatus(),
                                context.getDittoHeaders()
                        );
                    }
            );

    private final ThingId thingId;
    private final String featureId;

    private DeleteFeatureDefinitionResponse(final ThingId thingId,
            final String featureId,
            final HttpStatus httpStatus,
            final DittoHeaders dittoHeaders) {

        super(TYPE, httpStatus, dittoHeaders);
        this.thingId = thingId;
        this.featureId = checkNotNull(featureId, "featureId");
    }

    /**
     * Creates a response to a {@link DeleteFeatureDefinition} command.
     *
     * @param thingId the Thing ID of the deleted Feature Definition.
     * @param featureId the {@code Feature}'s ID whose Definition were deleted.
     * @param dittoHeaders the headers of the preceding command.
     * @return the response.
     * @throws NullPointerException if any argument is {@code null}.
     */
    public static DeleteFeatureDefinitionResponse of(final ThingId thingId,
            final String featureId,
            final DittoHeaders dittoHeaders) {

        return new DeleteFeatureDefinitionResponse(thingId, featureId, HTTP_STATUS, dittoHeaders);
    }

    /**
     * Creates a response to a {@link DeleteFeatureDefinition} command from a JSON string.
     *
     * @param jsonString the JSON string of which the response is to be created.
     * @param dittoHeaders the headers of the preceding command.
     * @return the response.
     * @throws NullPointerException if any argument is {@code null}.
     * @throws IllegalArgumentException if {@code jsonString} is empty.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonString} was not in the expected
     * format.
     * @throws org.eclipse.ditto.json.JsonMissingFieldException if the parsed {@code jsonString} did not contain any of
     * the required fields
     * <ul>
     *     <li>{@link org.eclipse.ditto.things.model.signals.commands.ThingCommandResponse.JsonFields#JSON_THING_ID} or</li>
     *     <li>{@link #JSON_FEATURE_ID}.</li>
     * </ul>
     * @throws org.eclipse.ditto.things.model.ThingIdInvalidException if the parsed thing ID did not comply to
     * {@link org.eclipse.ditto.base.model.entity.id.RegexPatterns#ID_REGEX}.
     */
    public static DeleteFeatureDefinitionResponse fromJson(final String jsonString, final DittoHeaders dittoHeaders) {
        return fromJson(JsonObject.of(jsonString), dittoHeaders);
    }

    /**
     * Creates a response to a {@link DeleteFeatureDefinition} command from a JSON object.
     *
     * @param jsonObject the JSON object of which the response is to be created.
     * @param dittoHeaders the headers of the preceding command.
     * @return the response.
     * @throws NullPointerException any argument is {@code null}.
     * @throws org.eclipse.ditto.json.JsonParseException if the passed in {@code jsonObject} was not in the expected
     * format.
     * @throws org.eclipse.ditto.json.JsonMissingFieldException if {@code jsonObject} did not contain any of the
     * required fields
     * <ul>
     *     <li>{@link org.eclipse.ditto.things.model.signals.commands.ThingCommandResponse.JsonFields#JSON_THING_ID} or</li>
     *     <li>{@link #JSON_FEATURE_ID}.</li>
     * </ul>
     * @throws org.eclipse.ditto.things.model.ThingIdInvalidException if the parsed thing ID did not comply to
     * {@link org.eclipse.ditto.base.model.entity.id.RegexPatterns#ID_REGEX}.
     */
    public static DeleteFeatureDefinitionResponse fromJson(final JsonObject jsonObject,
            final DittoHeaders dittoHeaders) {

        return JSON_DESERIALIZER.deserialize(jsonObject, dittoHeaders);
    }

    @Override
    public ThingId getEntityId() {
        return thingId;
    }

    /**
     * Returns the {@code Feature}'s ID whose Definition was deleted.
     *
     * @return the Feature's ID.
     */
    public String getFeatureId() {
        return featureId;
    }

    @Override
    public JsonPointer getResourcePath() {
        return JsonPointer.of("/features/" + featureId + "/definition");
    }

    @Override
    protected void appendPayload(final JsonObjectBuilder jsonObjectBuilder,
            final JsonSchemaVersion schemaVersion,
            final Predicate<JsonField> thePredicate) {

        final Predicate<JsonField> predicate = schemaVersion.and(thePredicate);
        jsonObjectBuilder.set(ThingCommandResponse.JsonFields.JSON_THING_ID, thingId.toString(), predicate);
        jsonObjectBuilder.set(JSON_FEATURE_ID, featureId, predicate);
    }

    @Override
    public DeleteFeatureDefinitionResponse setDittoHeaders(final DittoHeaders dittoHeaders) {
        return of(thingId, featureId, dittoHeaders);
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeleteFeatureDefinitionResponse that = (DeleteFeatureDefinitionResponse) o;
        return that.canEqual(this) &&
                Objects.equals(thingId, that.thingId) &&
                Objects.equals(featureId, that.featureId) &&
                super.equals(o);
    }

    @Override
    protected boolean canEqual(@Nullable final Object other) {
        return other instanceof DeleteFeatureDefinitionResponse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), thingId, featureId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + super.toString() + ", thingId=" + thingId + ", featureId=" +
                featureId + "]";
    }

}

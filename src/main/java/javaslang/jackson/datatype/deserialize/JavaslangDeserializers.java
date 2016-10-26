/**
 * Copyright 2015 The Javaslang Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javaslang.jackson.datatype.deserialize;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.collection.*;
import javaslang.control.Either;
import javaslang.control.Option;
import javaslang.jackson.datatype.JavaslangModule;
import javaslang.λ;

public class JavaslangDeserializers extends Deserializers.Base {

    private final JavaslangModule.Settings settings;

    public JavaslangDeserializers(JavaslangModule.Settings settings) {
        this.settings = settings;
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(JavaType type,
                                                    DeserializationConfig config,
                                                    BeanDescription beanDesc) throws JsonMappingException {
        Class<?> raw = type.getRawClass();
        if (CharSeq.class.isAssignableFrom(raw)) {
            return new CharSeqDeserializer(type);
        }
        if (PriorityQueue.class.isAssignableFrom(raw)) {
            return new PriorityQueueDeserializer(type, settings.deserializeNullAsEmptyCollection());
        }
        if (Lazy.class.isAssignableFrom(raw)) {
            return new LazyDeserializer(type);
        }
        if (Option.class.isAssignableFrom(raw)) {
            return new OptionDeserializer(type, settings.plainOption());
        }
        if (Either.class.isAssignableFrom(raw)) {
            return new EitherDeserializer(type);
        }
        if (Map.class.isAssignableFrom(raw)) {
            return new MapDeserializer(type);
        }
        if (Multimap.class.isAssignableFrom(raw)) {
            return new MultimapDeserializer(type);
        }
        if (Tuple.class.isAssignableFrom(raw)) {
            return new TupleDeserializer(type);
        }
        if (λ.class.isAssignableFrom(raw)) {
            return new SerializableDeserializer<>(type);
        }

        return super.findBeanDeserializer(type, config, beanDesc);
    }

    @Override
    public JsonDeserializer<?> findCollectionLikeDeserializer(CollectionLikeType type,
                                                              DeserializationConfig config, BeanDescription beanDesc,
                                                              TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
            throws JsonMappingException
    {
        Class<?> raw = type.getRawClass();
        if (Seq.class.isAssignableFrom(raw)) {
            return new SeqDeserializer(type, settings.deserializeNullAsEmptyCollection());
        }
        if (Set.class.isAssignableFrom(raw)) {
            return new SetDeserializer(type, settings.deserializeNullAsEmptyCollection());
        }
        return super.findCollectionLikeDeserializer(type, config, beanDesc, elementTypeDeserializer, elementDeserializer);
    }
}

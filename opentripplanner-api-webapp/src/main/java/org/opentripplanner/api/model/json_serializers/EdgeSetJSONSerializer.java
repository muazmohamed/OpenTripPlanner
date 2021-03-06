/* This program is free software: you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License
 as published by the Free Software Foundation, either version 3 of
 the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package org.opentripplanner.api.model.json_serializers;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;
import org.opentripplanner.routing.core.Vertex;


/**
 * This serializes an object containing Edge objects, replacing any vertices with their (unique) labels
 * @see VertexSetJSONSerializer
 * @author novalis
 *
 */
public class EdgeSetJSONSerializer extends JsonSerializer<WithGraph> {

    @Override
    public void serialize(WithGraph object, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        // FIXME: there is probably a simple way to automatically wire this up that I can't find
        // right now 
        ObjectMapper mapper = SerializerUtils.getMapper();

        SimpleModule module = SerializerUtils.getSerializerModule();
        module.addSerializer(new VertexSerializer());
        
        mapper.registerModule(module);
        
        //configuring jgen to just use the mapper doesn't actually work
        jgen.writeRawValue(mapper.writeValueAsString(object.getObject()));
    }
    
    class VertexSerializer extends JsonSerializer<Vertex> {

        @Override
        public void serialize(Vertex value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeObject(value.getLabel());
        }
        
        @Override
        public Class<Vertex> handledType() {
            return Vertex.class;
        }
        
    }
}

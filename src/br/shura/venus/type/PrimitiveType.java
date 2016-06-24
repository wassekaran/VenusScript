//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// Licensed under the GNU General Public License v3;                                     /
// you may not use this file except in compliance with the License.                      /
//                                                                                       /
// You may obtain a copy of the License at                                               /
//     http://www.gnu.org/licenses/gpl.html                                              /
//                                                                                       /
// Unless required by applicable law or agreed to in writing, software                   /
// distributed under the License is distributed on an "AS IS" BASIS,                     /
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.              /
// See the License for the specific language governing permissions and                   /
// limitations under the License.                                                        /
//                                                                                       /
// Written by João Vitor Verona Biazibetti <joaaoverona@gmail.com>, March 2016           /
// https://www.github.com/BloodShura                                                     /
//////////////////////////////////////////////////////////////////////////////////////////

package br.shura.venus.type;

import br.shura.venus.expression.Variable;
import br.shura.venus.function.Function;
import br.shura.venus.value.ArrayValue;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.DecimalValue;
import br.shura.venus.value.FunctionRefValue;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.StringValue;
import br.shura.venus.value.TypeValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.VariableRefValue;
import br.shura.x.collection.view.ArrayView;
import br.shura.x.collection.view.View;
import br.shura.x.util.layer.XApi;
import br.shura.x.worker.UtilWorker;
import br.shura.x.worker.enumeration.Enumerations;

/**
 * PrimitiveType.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 29/05/16 - 17:40
 * @since GAMMA - 0x3
 */
public final class PrimitiveType extends Type {
  public static final Type ANY = new PrimitiveType("any", Value.class, Object.class);
  public static final Type ARRAY = new PrimitiveType("array", ArrayValue.class, Value[].class);
  public static final Type BOOLEAN = new PrimitiveType("bool", BoolValue.class, Boolean.class);
  public static final Type DECIMAL = new PrimitiveType("decimal", DecimalValue.class, Double.class, Float.class);
  public static final Type FUNCTION_REFERENCE = new PrimitiveType("ref", FunctionRefValue.class, Function.class);
  public static final Type INTEGER = new PrimitiveType("int", IntegerValue.class, Integer.class, Long.class);
  public static final Type STRING = new PrimitiveType("string", StringValue.class, String.class);
  public static final Type TYPE = new PrimitiveType("type", TypeValue.class, Type.class);
  public static final Type VARIABLE_REFERENCE = new PrimitiveType("var", VariableRefValue.class, Variable.class);

  private final View<Class<?>> objectTypes;
  private final Class<? extends Value> valueClass;

  private PrimitiveType(String identifier, Class<? extends Value> valueClass, Class<?>... objectTypes) {
    super(identifier);
    this.objectTypes = new ArrayView<>(objectTypes);
    this.valueClass = valueClass;
  }

  @Override
  public boolean accepts(Class<? extends Value> valueCl) {
    return valueClass.isAssignableFrom(valueCl);
  }

  @Override
  public boolean accepts(Type type) {
    return type instanceof PrimitiveType && accepts(((PrimitiveType) type).valueClass);
  }

  @Override
  public boolean objectAccepts(Class<?> type) {
    return objectTypes.any(object -> object.isAssignableFrom(UtilWorker.fixPrimitiveClass(type)));
  }

  public static Type forIdentifier(String identifier) {
    XApi.requireNonNull(identifier, "identifier");

    for (Type value : valuesView()) {
      if (value.getIdentifier().equals(identifier)) {
        return value;
      }
    }

    return null;
  }

  public static Type forObjectType(Class<?> type) {
    XApi.requireNonNull(type, "type");

    for (Type value : valuesView()) {
      if (value != ANY && value.objectAccepts(type)) {
        return value;
      }
    }

    return ANY;
  }

  public static Type forType(Class<? extends Value> type) {
    XApi.requireNonNull(type, "type");

    for (Type value : valuesView()) {
      if (value != ANY && value.accepts(type)) {
        return value;
      }
    }

    return ANY;
  }

  public static View<PrimitiveType> valuesView() {
    return Enumerations.values(PrimitiveType.class);
  }
}
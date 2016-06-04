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

package br.shura.venus.value;

import br.shura.venus.type.PrimitiveType;

/**
 * IntegerValue.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 09/05/16 - 01:48
 * @since GAMMA - 0x3
 */
public class IntegerValue extends NumericValue {
  private final long value;

  public IntegerValue(long value) {
    super(PrimitiveType.INTEGER);
    this.value = value;
  }

  @Override
  public IntegerValue and(Value value) {
    if (value instanceof IntegerValue) {
      return new IntegerValue(value() & ((IntegerValue) value).value());
    }

    return null;
  }

  @Override
  public IntegerValue clone() {
    return new IntegerValue(value());
  }

  @Override
  public Integer compareTo(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return value().compareTo(numeric.value().longValue());
    }

    return null;
  }

  @Override
  public IntegerValue divide(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new IntegerValue(value() / numeric.value().longValue());
    }

    return null;
  }

  @Override
  public IntegerValue minus(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new IntegerValue(value() - numeric.value().longValue());
    }

    return null;
  }

  @Override
  public IntegerValue multiply(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new IntegerValue(value() * numeric.value().longValue());
    }

    return null;
  }

  @Override
  public IntegerValue negate() {
    return new IntegerValue(-value());
  }

  @Override
  public IntegerValue or(Value value) {
    if (value instanceof IntegerValue) {
      return new IntegerValue(value() | ((IntegerValue) value).value());
    }

    return null;
  }

  @Override
  public Value shiftLeft(Value value) {
    if (value instanceof IntegerValue) {
      IntegerValue integer = (IntegerValue) value;

      return new IntegerValue(value() << integer.value());
    }

    return super.shiftLeft(value);
  }

  @Override
  public Value shiftRight(Value value) {
    if (value instanceof IntegerValue) {
      IntegerValue integer = (IntegerValue) value;

      return new IntegerValue(value() >> integer.value());
    }

    return super.shiftRight(value);
  }

  @Override
  public Value plus(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new IntegerValue(value() + numeric.value().longValue());
    }

    return super.plus(value);
  }

  @Override
  public IntegerValue remainder(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new IntegerValue(value() % numeric.value().longValue());
    }

    return null;
  }

  @Override
  public String toString() {
    return Long.toString(value());
  }

  @Override
  public Long value() {
    return value;
  }
}
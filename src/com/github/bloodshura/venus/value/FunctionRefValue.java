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

package com.github.bloodshura.venus.value;

import com.github.bloodshura.venus.compiler.KeywordDefinitions;
import com.github.bloodshura.venus.type.PrimitiveType;
import com.github.bloodshura.x.util.layer.XApi;

/**
 * FunctionRefValue.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 14/05/16 - 02:38
 * @since GAMMA - 0x3
 */
public class FunctionRefValue extends Value {
  private final String value;

  public FunctionRefValue(String value) {
    super(PrimitiveType.FUNCTION_REFERENCE);
    XApi.requireNonNull(value, "value");

    this.value = value;
  }

  @Override
  public FunctionRefValue clone() {
    return new FunctionRefValue(value());
  }

  @Override
  public String toString() {
    return KeywordDefinitions.FUNCTION_REFERENCE + value();
  }

  @Override
  public String value() {
    return value;
  }
}
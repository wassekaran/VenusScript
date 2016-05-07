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

import br.shura.venus.Context;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.x.util.layer.XApi;

/**
 * Variable.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 01:34
 * @since GAMMA - 0x3
 */
public class Variable extends Value {
  private final String name;

  public Variable(String name) {
    XApi.requireNonNull(name, "name");

    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public Object resolve(Context context) throws ScriptRuntimeException {
    return context.getVarValue(getName());
  }

  @Override
  public String toString() {
    return "var(" + getName() + ')';
  }
}
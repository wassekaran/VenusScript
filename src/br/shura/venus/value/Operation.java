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
import br.shura.venus.operator.Operator;

/**
 * Operation.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 02:18
 * @since GAMMA - 0x3
 */
public class Operation extends Value {
  private final Value left;
  private final Operator operator;
  private final Value right;

  public Operation(Operator operator, Value left, Value right) {
    this.left = left;
    this.operator = operator;
    this.right = right;
  }

  public Value getLeft() {
    return left;
  }

  public Operator getOperator() {
    return operator;
  }

  public Value getRight() {
    return right;
  }

  @Override
  public Object resolve(Context context) throws ScriptRuntimeException {
    return getOperator().operate(context, getLeft(), getRight());
  }

  @Override
  public String toString() {
    return "operation([" + getLeft() + "] " + getOperator() + " [" + getRight() + "])";
  }
}
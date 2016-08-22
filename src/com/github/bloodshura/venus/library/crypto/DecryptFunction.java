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

package com.github.bloodshura.venus.library.crypto;

import com.github.bloodshura.crypto.Decrypter;
import com.github.bloodshura.crypto.exception.CryptoException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.Function;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.type.PrimitiveType;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.BoolValue;
import com.github.bloodshura.venus.value.StringValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.venus.value.VariableRefValue;
import com.github.bloodshura.x.collection.view.ArrayView;
import com.github.bloodshura.x.collection.view.View;

/**
 * DecryptFunction.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 17/05/16 - 13:01
 * @since GAMMA - 0x3
 */
public class DecryptFunction implements Function {
  private final View<Type> argumentTypes;
  private final Decrypter decrypter;
  private final String name;

  public DecryptFunction(String name, Decrypter decrypter) {
    this.argumentTypes = new ArrayView<>(PrimitiveType.STRING, PrimitiveType.VARIABLE_REFERENCE);
    this.decrypter = decrypter;
    this.name = name;
  }

  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    StringValue value = (StringValue) descriptor.get(0);
    VariableRefValue reference = (VariableRefValue) descriptor.get(1);

    try {
      String result = getDecrypter().decryptToStr(value.value());

      context.setVar(reference.value(), new StringValue(result));

      return new BoolValue(true);
    }
    catch (CryptoException exception) {
      return new BoolValue(false);
    }
  }

  @Override
  public View<Type> getArgumentTypes() {
    return argumentTypes;
  }

  public Decrypter getDecrypter() {
    return decrypter;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isVarArgs() {
    return false;
  }
}
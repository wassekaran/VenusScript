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

package br.shura.venus.component;

import br.shura.venus.compiler.VenusParser;
import br.shura.venus.component.function.Function;
import br.shura.venus.exception.ScriptCompileException;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.executor.ApplicationContext;
import br.shura.venus.executor.Context;
import br.shura.venus.library.LibraryList;
import br.shura.venus.origin.ScriptOrigin;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.collection.view.View;
import br.shura.x.logging.XLogger;

/**
 * Script.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 01:36
 * @since GAMMA - 0x3
 */
public class Script extends Container {
  private final ApplicationContext appContext;
  private final List<Script> includes;
  private final LibraryList libraryList;
  private final ScriptOrigin origin;
  private final VenusParser parser;

  public Script(ApplicationContext appContext, ScriptOrigin origin) {
    this.appContext = appContext;
    this.context = new Context(this, null);
    this.includes = new ArrayList<>();
    this.libraryList = new LibraryList();
    this.origin = origin;
    this.parser = new VenusParser(this);
  }

  @Override
  public Function findFunction(Context context, String name, View<ValueType> argumentTypes) throws ScriptRuntimeException {
    try {
      return super.findFunction(context, name, argumentTypes);
    }
    catch (ScriptRuntimeException exception) {
      for (Script script : getIncludes()) {
        try {
          return script.findFunction(context, name, argumentTypes);
        }
        catch (ScriptRuntimeException exception2) {
        }
      }

      Function function = getLibraryList().findFunction(name, argumentTypes);

      if (function != null) {
        return function;
      }

      throw exception;
    }
  }

  @Override
  public ApplicationContext getApplicationContext() {
    return appContext;
  }

  @Override
  public String getDisplayName() {
    return getOrigin().getScriptName();
  }

  public List<Script> getIncludes() {
    return includes;
  }

  public LibraryList getLibraryList() {
    return libraryList;
  }

  public ScriptOrigin getOrigin() {
    return origin;
  }

  public VenusParser getParser() {
    return parser;
  }

  @Override
  public Script getScript() {
    return this;
  }

  public void include(String includeName, boolean maybe) throws ScriptCompileException {
    ScriptOrigin origin = getOrigin().findInclude(includeName);

    if (origin != null) {
      Script script = origin.compile(getApplicationContext());

      getIncludes().add(script);
    }
    else if (maybe) {
      XLogger.debugln("Not found include script \"" + includeName + "\", but it was marked as maybe.");
    }
    else {
      throw new ScriptCompileException("Could not find script \"" + includeName + "\"");
    }
  }

  @Deprecated
  @Override
  public void setParent(Container parent) {
    throw new IllegalStateException("Cannot define a script's parent");
  }

  @Override
  public String toString() {
    return "script(name=" + getDisplayName() + ", origin=" + getOrigin() + ", includes=" + getIncludes() + ')';
  }
}
package com.example.echo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Author:Ruibo Liu(ruibo.liu.gr@dartmoputh.edu)
 *
 * Licensed under the Apache License,Version2.0(the"License");
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
public class CreateNoteActivity extends SingleFragmentActivity {
    private static final String EXTRA_NOTE_ID =
            "com.example.echo.note_id";

    /**
     * helper static method to create intent with argument
     *
     * @param packageContext
     * @param noteID
     * @return
     */
    public static Intent newIntent(Context packageContext, UUID noteID) {
        Intent intent = new Intent(packageContext, CreateNoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID noteID = (UUID) getIntent()
                .getSerializableExtra(EXTRA_NOTE_ID);
        return CreateNoteFragment.newInstance(noteID);
    }
}

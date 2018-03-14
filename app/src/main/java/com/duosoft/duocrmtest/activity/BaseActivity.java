/*
 * Copyright (C) 2017, Duo Software Pvt Ltd.
 *
 * Licensed under the Duo Software;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.duosoftworld.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.duosoft.duocrmtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.duosoft.duocrmtest.R;

/**
 * Created by mihira on 6/19/17.
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * Navigate to preferred class
     *
     * @param child
     */
    protected void navigateActivity(Class<?> child) {
        if (child == null)
            return;

        Intent intent = new Intent();
        intent.setClass(this, child);

        startActivityForResult(intent, 0);

    }

    /**
     * Navigate to preferred class
     *
     * @param child
     */
    protected void navigateDynamicActivity(Class<?> child, String id) {
        if (child == null)
            return;

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        intent.putExtras(bundle);
        intent.setClass(this, child);

        startActivityForResult(intent, 0);
    }

    // Close activity
    public void closeAitvity() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }
}

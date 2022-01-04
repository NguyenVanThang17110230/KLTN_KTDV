import React, { ReactElement } from "react";

import { CellProps, Column, useTable } from "react-table";
import { COLUMN_ACOUNT } from "../../package/account/columns/Columns";
import { Account } from "../../package/account/model/Account";
import { AdminLayout } from "../../layouts/Admin";

export default function AdminDashboard() {
  return (
    <div className="py-10 px-20 grid grid-cols-3 gap-5">
      <div className="px-4 py-8 rounded shadow-md bg-gray-200 flex justify-between">
        <div className="w-16 h-16 bg-gray-800 rounded-md flex items-center justify-center">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="icon icon-tabler icon-tabler-users"
            width={28}
            height={28}
            viewBox="0 0 24 24"
            strokeWidth={2}
            stroke="#ffffff"
            fill="none"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path stroke="none" d="M0 0h24v24H0z" fill="none" />
            <circle cx={9} cy={7} r={4} />
            <path d="M3 21v-2a4 4 0 0 1 4 -4h4a4 4 0 0 1 4 4v2" />
            <path d="M16 3.13a4 4 0 0 1 0 7.75" />
            <path d="M21 21v-2a4 4 0 0 0 -3 -3.85" />
          </svg>
        </div>
        <div>
          <div className="font-medium text-xl">Number of users</div>
          <div className="text-right mt-3 text-lg">8</div>
        </div>
      </div>
      <div className="px-4 py-8 rounded shadow-md bg-gray-200 flex justify-between">
        <div className="w-16 h-16 bg-gray-800 rounded-md flex items-center justify-center">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="icon icon-tabler icon-tabler-files"
            width={28}
            height={28}
            viewBox="0 0 24 24"
            strokeWidth={2}
            stroke="#ffffff"
            fill="none"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path stroke="none" d="M0 0h24v24H0z" fill="none" />
            <path d="M15 3v4a1 1 0 0 0 1 1h4" />
            <path d="M18 17h-7a2 2 0 0 1 -2 -2v-10a2 2 0 0 1 2 -2h4l5 5v7a2 2 0 0 1 -2 2z" />
            <path d="M16 17v2a2 2 0 0 1 -2 2h-7a2 2 0 0 1 -2 -2v-10a2 2 0 0 1 2 -2h2" />
          </svg>
        </div>
        <div>
          <div className="font-medium text-xl">Number of account</div>
          <div className="text-right mt-3 text-lg">81</div>
        </div>
      </div>
      <div className="px-4 py-8 rounded shadow-md bg-gray-200 flex justify-between">
        <div className="w-16 h-16 bg-gray-800 rounded-md flex items-center justify-center">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="icon icon-tabler icon-tabler-files"
            width={28}
            height={28}
            viewBox="0 0 24 24"
            strokeWidth={2}
            stroke="#ffffff"
            fill="none"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path stroke="none" d="M0 0h24v24H0z" fill="none" />
            <path d="M15 3v4a1 1 0 0 0 1 1h4" />
            <path d="M18 17h-7a2 2 0 0 1 -2 -2v-10a2 2 0 0 1 2 -2h4l5 5v7a2 2 0 0 1 -2 2z" />
            <path d="M16 17v2a2 2 0 0 1 -2 2h-7a2 2 0 0 1 -2 -2v-10a2 2 0 0 1 2 -2h2" />
          </svg>
        </div>
        <div>
          <div className="font-medium text-xl">Number of account</div>
          <div className="text-right mt-3 text-lg">81</div>
        </div>
      </div>
    </div>
  );
}

AdminDashboard.getLayout = function getLayout(page) {
  return <AdminLayout>{page}</AdminLayout>;
};

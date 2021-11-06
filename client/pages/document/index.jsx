import React, { useState } from "react";
import Head from "next/head";
import { UserLayout } from "../../layouts/User";
import DocumentFile from "../../package/document/component/Document";
import AddNewDocumentModal from "../../package/document/component/AddNewDocumentModal";

const Document = () => {
  const [isShowAddNewDocument, setIsShowAddNewDocument] = useState(false);

  return (
    <>
      <Head>
        <title>Document</title>
      </Head>
      <div className="px-20 py-10">
        <div className="shadow-md p-3">
          <div className="flex items-center justify-between mb-4">
            <div className="font-medium text-2xl text-transparent bg-clip-text bg-gradient-to-r from-green-400 to-blue-500">
              List document
            </div>
            <div
              className="flex p-3 items-center bg-gradient-to-r from-green-400 to-blue-500 hover:from-pink-500 hover:to-yellow-500 cursor-pointer rounded-md relative"
              onClick={() => setIsShowAddNewDocument(true)}
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="icon icon-tabler icon-tabler-file-plus"
                width={26}
                height={26}
                viewBox="0 0 24 24"
                strokeWidth="1.5"
                stroke="#ffffff"
                fill="none"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <path stroke="none" d="M0 0h24v24H0z" fill="none" />
                <path d="M14 3v4a1 1 0 0 0 1 1h4" />
                <path d="M17 21h-10a2 2 0 0 1 -2 -2v-14a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z" />
                <line x1={12} y1={11} x2={12} y2={17} />
                <line x1={9} y1={14} x2={15} y2={14} />
              </svg>
              <div className="ml-2 font-medium">Add new document</div>
            </div>
          </div>
          <DocumentFile />
        </div>
      </div>
      {isShowAddNewDocument && <AddNewDocumentModal  closeModal={() => setIsShowAddNewDocument(false)} />}
    </>
  );
};

Document.getLayout = function getLayout(page) {
  return <UserLayout>{page}</UserLayout>;
};

export default Document;